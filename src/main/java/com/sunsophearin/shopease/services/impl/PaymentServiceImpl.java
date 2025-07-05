package com.sunsophearin.shopease.services.impl;

import com.sunsophearin.shopease.dto.PaymentRequest;
import com.sunsophearin.shopease.dto.SaleDto;
import com.sunsophearin.shopease.entities.Sale;
import com.sunsophearin.shopease.security.entities.User;
import com.sunsophearin.shopease.security.service.impl.UserServiceImpl;
import com.sunsophearin.shopease.services.PaymentService;
import com.sunsophearin.shopease.services.SaleService;
import com.sunsophearin.shopease.util.TelegramMessageBuilder;
import jakarta.annotation.PreDestroy;
import kh.org.nbc.bakong_khqr.BakongKHQR;
import kh.org.nbc.bakong_khqr.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final SimpMessagingTemplate messagingTemplate;
    private final SaleService saleService;
    private final UserServiceImpl userService;
    private final TelegramBotService telegramBotService;
    private final RestTemplate restTemplate = new RestTemplate();

    private final Map<String, ScheduledFuture<?>> runningTasks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Value("${bakong.token}") private String bakongToken;
    @Value("${bakong.account.id}") private String bakongAccountId;
    @Value("${merchant.name}") private String merchantName;
    @Value("${merchant.city}") private String merchantCity;
    @Value("${merchant.mobile}") private String merchantMobile;

    private static final String PAYMENT_TOPIC = "/topic/khqr-payment";
    private static final String BAKONG_PAYMENT_URL = "https://api-bakong.nbc.gov.kh/v1/check_transaction_by_md5";

    @Override
    @Transactional
    public Map<String, Object> generateKhqr(SaleDto saleDto, String userEmail) {
        userService.findUserName(userEmail);

        // Enrich sale details with product info (names, prices, etc.)
        saleService.enrichSaleDetailDTOs(saleDto);

        // Calculate total price after enrichment
        BigDecimal totalAmount = saleService.calculateTotalPrice(saleDto);
        log.info("total amount {}", totalAmount);

        IndividualInfo qrInfo = buildQrInfo(totalAmount);
        KHQRResponse<KHQRData> qrResponse = BakongKHQR.generateIndividual(qrInfo);

        if (qrResponse.getKHQRStatus().getCode() == 0) {
            String qr = qrResponse.getData().getQr();
            String md5 = qrResponse.getData().getMd5();

            schedulePaymentPolling(saleDto, userEmail, md5);
            return Map.of("qr", qr, "md5", md5);
        }

        log.error("Failed to generate KHQR: {}", qrResponse.getKHQRStatus());
        throw new RuntimeException("Failed to generate QR: " + qrResponse.getKHQRStatus().getMessage());
    }

    private IndividualInfo buildQrInfo(BigDecimal totalAmount) {
        IndividualInfo info = new IndividualInfo();
        info.setBakongAccountId(bakongAccountId);
        info.setCurrency(KHQRCurrency.USD);
        info.setAmount(totalAmount.doubleValue());
        info.setMerchantName(merchantName);
        info.setMerchantCity(merchantCity);
        info.setMobileNumber(merchantMobile);
        info.setStoreLabel("Mrr.Black");
        return info;
    }

    private void schedulePaymentPolling(SaleDto saleDto, String userEmail, String md5) {
        if (runningTasks.containsKey(md5)) {
            log.info("Polling already scheduled for md5: {}", md5);
            return;
        }

        Runnable task = () -> {
            try {
                log.info("Polling payment for md5: {}", md5);
                Map<String, Object> paymentStatus = checkPayment(md5);

                if (isPaymentSuccessful(paymentStatus)) {
                    // Get the saved Sale with transactionId
                    Sale savedSale = saleService.processSale(saleDto, userEmail);

                    saleService.enrichSaleDetailDTOs(saleDto);

                    BigDecimal totalAmount = saleService.calculateTotalPrice(saleDto);
                    String notifyMsg = TelegramMessageBuilder.buildOrderNotification(
                            savedSale, // Pass transactionId from entity
                            saleDto,
                            userEmail,
                            "PAID",
                            totalAmount
                    );
                    telegramBotService.sendOrderNotification(notifyMsg);

                    // Notify frontend via WebSocket
                    messagingTemplate.convertAndSend(
                            PAYMENT_TOPIC,
                            Map.of("md5", md5, "status", "PAID")
                    );

                    cancelPolling(md5);
                }
            } catch (Exception e) {
                log.error("Payment polling error for md5 {}: {}", md5, e.getMessage(), e);
            }
        };

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(task, 5, 5, TimeUnit.SECONDS);
        runningTasks.put(md5, future);

        // Auto-cancel after 2 minutes
        scheduler.schedule(() -> cancelPolling(md5), 2, TimeUnit.MINUTES);
    }


    private boolean isPaymentSuccessful(Map<String, Object> paymentStatus) {
        return paymentStatus != null &&
                Integer.valueOf(0).equals(paymentStatus.get("responseCode"));
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
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + bakongToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> request =
                    new HttpEntity<>(Map.of("md5", md5), headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    BAKONG_PAYMENT_URL,
                    request,
                    Map.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to check payment for md5 {}: {}", md5, e.getMessage(), e);
            throw new RuntimeException("Failed to check payment status");
        }
    }
    @PreDestroy
    public void shutdownScheduler() {
        log.info("Shutting down scheduler...");
        runningTasks.values().forEach(future -> future.cancel(true));
        scheduler.shutdownNow();
    }
}
