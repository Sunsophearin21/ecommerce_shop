package com.sunsophearin.shopease.services.impl;

import com.sunsophearin.shopease.dto.PaymentRequest;
import com.sunsophearin.shopease.dto.SaleDto;
import com.sunsophearin.shopease.entities.Product;
import com.sunsophearin.shopease.entities.ProductVariant;
import com.sunsophearin.shopease.entities.Sale;
import com.sunsophearin.shopease.entities.Stock;
import com.sunsophearin.shopease.exception.ResoureApiNotFound;
import com.sunsophearin.shopease.repositories.ProductVariantRepository;
import com.sunsophearin.shopease.repositories.SaleRepository;
import com.sunsophearin.shopease.repositories.StockRepository;
import com.sunsophearin.shopease.security.entities.User;
import com.sunsophearin.shopease.security.service.impl.UserServiceImpl;
import com.sunsophearin.shopease.services.PaymentService;
import com.sunsophearin.shopease.services.ProductService;
import jakarta.annotation.PreDestroy;
import kh.org.nbc.bakong_khqr.BakongKHQR;
import kh.org.nbc.bakong_khqr.model.IndividualInfo;
import kh.org.nbc.bakong_khqr.model.KHQRCurrency;
import kh.org.nbc.bakong_khqr.model.KHQRData;
import kh.org.nbc.bakong_khqr.model.KHQRResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
//@Service
@RequiredArgsConstructor
public class PaymentServiceImpl2 implements PaymentService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ProductService productService;
    private final SaleRepository saleRepo;
    private final ProductVariantRepository variantRepo;
    private final StockRepository stockRepo;
    private final UserServiceImpl userService;
    private final SaleServiceImpl saleService;
    private final RestTemplate restTemplate = new RestTemplate();

    private final Map<String, ScheduledFuture<?>> runningTasks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Value("${bakong.token}")
    private String bakongToken;

    @Value("${bakong.account.id}")
    private String bakongAccountId;

    @Value("${merchant.name}")
    private String merchantName;

    @Value("${merchant.city}")
    private String merchantCity;

    @Value("${merchant.mobile}")
    private String merchantMobile;

    private static final String PAYMENT_TOPIC = "/topic/khqr-payment";
    private static final String BAKONG_PAYMENT_URL = "https://api-bakong.nbc.gov.kh/v1/check_transaction_by_md5";

    @Override
    public Map<String, Object> generateKhqr(SaleDto request, String userEmail) {
        return Map.of();
    }

    @Override
    @Transactional
    public Map<String, Object> generateKhqr2(PaymentRequest request, String userEmail) {
        userService.findUserName(userEmail);

        IndividualInfo qrInfo = buildQrInfo(request);
        KHQRResponse<KHQRData> qrResponse = BakongKHQR.generateIndividual(qrInfo);

        if (qrResponse.getKHQRStatus().getCode() == 0) {
            String qr = qrResponse.getData().getQr();
            String md5 = qrResponse.getData().getMd5();

            schedulePaymentPolling(request, userEmail, md5);
            return Map.of("qr", qr, "md5", md5);
        }

        log.error("Failed to generate KHQR: {}", qrResponse.getKHQRStatus());
        throw new RuntimeException("Failed to generate QR: " + qrResponse.getKHQRStatus().getMessage());
    }

    private IndividualInfo buildQrInfo(PaymentRequest request) {
        IndividualInfo info = new IndividualInfo();
        info.setBakongAccountId(bakongAccountId);
        info.setCurrency(KHQRCurrency.KHR);
        info.setAmount(request.getAmount());
        info.setMerchantName(merchantName);
        info.setMerchantCity(merchantCity);
        info.setBillNumber(request.getTransactionId());
        info.setMobileNumber(merchantMobile);
        info.setStoreLabel("Mrr.Black");
        return info;
    }

    private void schedulePaymentPolling(PaymentRequest request, String userEmail, String md5) {
        if (runningTasks.containsKey(md5)) {
            log.info("Polling already scheduled for md5: {}", md5);
            return;
        }

        Runnable task = () -> {
            try {
                log.info("Polling payment for md5: {}", md5);
                Map<String, Object> paymentStatus = checkPayment(md5);
                if (paymentStatus != null && Integer.valueOf(0).equals(paymentStatus.get("responseCode"))) {
//                    confirmAndSaveSale(request, userEmail, md5);
//                    saleService.processSale();

                    messagingTemplate.convertAndSend(PAYMENT_TOPIC, Map.of("md5", md5, "status", "PAID"));
                    cancelPolling(md5);
                }
            } catch (Exception e) {
                log.error("Error polling payment for md5 {}: {}", md5, e.getMessage(), e);
            }
        };

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(task, 5, 5, TimeUnit.SECONDS);
        runningTasks.put(md5, future);
        scheduler.schedule(() -> cancelPolling(md5), 2, TimeUnit.MINUTES);
    }

    private void cancelPolling(String md5) {
        ScheduledFuture<?> future = runningTasks.remove(md5);
        if (future != null) {
            future.cancel(true);
            log.info("Stopped polling for md5: {}", md5);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> checkPayment(String md5) {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of("md5", md5), headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(BAKONG_PAYMENT_URL, request, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to check payment for md5 {}: {}", md5, e.getMessage(), e);
            throw new RuntimeException("Failed to check payment status");
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bakongToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Override
    @Transactional
    public void confirmAndSaveSale(PaymentRequest dto, String userEmail, String md5) {
        User user = userService.findUserName(userEmail);
        Product product = productService.getProductById(dto.getProductId());

        ProductVariant variant = variantRepo.findByIdWithStocks(dto.getProductVariantId())
                .orElseThrow(() -> new ResoureApiNotFound("ProductVariant", dto.getProductVariantId()));

        Stock stock = variant.getStocks().stream()
                .filter(s -> s.getSize().getId().equals(dto.getSizeId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Size not found"));

        if (stock.getCurrentQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Not enough stock. Available: " + stock.getCurrentQuantity());
        }

        stock.setCurrentQuantity(stock.getCurrentQuantity() - dto.getQuantity());
        stockRepo.save(stock);

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setStatus("PAID");
        sale.setTransactionId(md5);
        sale.setFinalPrice(product.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity())));
        saleRepo.save(sale);
    }

    @PreDestroy
    public void shutdownScheduler() {
        log.info("Shutting down scheduler...");
        runningTasks.values().forEach(future -> future.cancel(true));
        scheduler.shutdownNow();
    }
}
