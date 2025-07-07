package com.sunsophearin.shopease.services.impl;

import com.sunsophearin.shopease.dto.SaleDetailDTO;
import com.sunsophearin.shopease.dto.SaleDto;
import com.sunsophearin.shopease.dto.response.SaleDtoResponse;
import com.sunsophearin.shopease.entities.*;
import com.sunsophearin.shopease.enums.DeliveryStatus;
import com.sunsophearin.shopease.exception.ResoureApiNotFound;
import com.sunsophearin.shopease.mapper.SaleMapper;
import com.sunsophearin.shopease.repositories.*;
import com.sunsophearin.shopease.security.entities.User;
import com.sunsophearin.shopease.security.service.impl.UserServiceImpl;
import com.sunsophearin.shopease.services.SaleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final ProductRepository productRepository;
    private final SaleRepository saleRepo;
    private final ProductVariantRepository variantRepo;
    private final StockRepository stockRepo;
    private final SaleDetailRepository saleDetailRepo;
    private final UserServiceImpl userService;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final SaleMapper saleMapper;

    @Override
    public void enrichSaleDetailDTOs(SaleDto saleDto) {
        if (saleDto == null || saleDto.getSaleDetailDTOS() == null) return;

        // Fetch all needed products in batch
        List<Long> productIds = saleDto.getSaleDetailDTOS().stream()
                .map(SaleDetailDTO::getProductId)
                .filter(Objects::nonNull)
                .toList();
        Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        // Fetch all needed colors and sizes in batch (optional, for performance)
        List<Long> colorIds = saleDto.getSaleDetailDTOS().stream()
                .map(SaleDetailDTO::getColorId)
                .filter(Objects::nonNull)
                .toList();
        Map<Long, Color> colorMap = colorRepository.findAllById(colorIds).stream()
                .collect(Collectors.toMap(Color::getId, Function.identity()));

        List<Long> sizeIds = saleDto.getSaleDetailDTOS().stream()
                .map(SaleDetailDTO::getSizeId)
                .filter(Objects::nonNull)
                .toList();
        Map<Long, Size> sizeMap = sizeRepository.findAllById(sizeIds).stream()
                .collect(Collectors.toMap(Size::getId, Function.identity()));

        // Set productName, unitPrice, colorName, and sizeName
        for (SaleDetailDTO detail : saleDto.getSaleDetailDTOS()) {
            Product product = productMap.get(detail.getProductId());
            if (product == null) throw new RuntimeException("Product not found: " + detail.getProductId());
            detail.setProductName(product.getName());
            detail.setUnitPrice(product.getFinalPrice());

            Color color = colorMap.get(detail.getColorId());
            detail.setColorName(color != null ? color.getName() : null);

            Size size = sizeMap.get(detail.getSizeId());
            detail.setSizeName(size != null ? size.getName() : null);
        }
    }

    @Override
    @Transactional
    public Sale processSale(SaleDto saleDto, String userEmail) {
        enrichSaleDetailDTOs(saleDto); // Always enrich before processing
        User user = userService.findUserName(userEmail);

        Map<Long, ProductVariant> variantMap = fetchVariants(saleDto);
        ProcessingResult result = processItems(saleDto, variantMap);

        // Update stock
        for (SaleDetailDTO detail : saleDto.getSaleDetailDTOS()) {
            ProductVariant variant = variantMap.get(detail.getVariantId());
            Stock stock = getStockOrThrow(detail, variant);
            stock.setCurrentQuantity(stock.getCurrentQuantity() - detail.getQuantity());
            stockRepo.save(stock);
        }
        stockRepo.saveAll(result.stocksToUpdate());

        // --- FIX: Always set transactionId before saving ---
        Sale sale = createSaleRecord(result, user);
        if (sale.getTransactionId() == null || sale.getTransactionId().isEmpty()) {
            sale.setTransactionId("tst" + UUID.randomUUID().toString().replace("-", ""));
        }
        Sale savedSale = saleRepo.save(sale);
        saveSaleDetails(result.saleDetails(), savedSale);
        return savedSale;
    }

    @Override
    public BigDecimal calculateTotalPrice(SaleDto saleDto) {
        if (saleDto == null || saleDto.getSaleDetailDTOS() == null) return BigDecimal.ZERO;
        enrichSaleDetailDTOs(saleDto);
        return saleDto.getSaleDetailDTOS().stream()
                .filter(detail -> detail.getUnitPrice() != null && detail.getQuantity() != null)
                .map(detail -> detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    @Override
    public BigDecimal calculateTotalPriceWithDelivery(SaleDto saleDto) {
        Map<Long, ProductVariant> variantMap = fetchVariants(saleDto);
        processItems(saleDto, variantMap);
        BigDecimal productTotal = calculateTotalPrice(saleDto);

        // Collect delivery fees from all products in the order
        Set<BigDecimal> deliveryFees = saleDto.getSaleDetailDTOS().stream()
                .map(detail -> {
                    // You may need to fetch the product entity here if not already available
                    Product product = productRepository.findById(detail.getProductId()).orElse(null);
                    return product != null ? product.getDeliveryFee() : BigDecimal.ZERO;
                })
                .filter(Objects::nonNull)
                .filter(fee -> fee.compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toSet());

        // Choose your business rule: e.g., highest delivery fee among products
        BigDecimal orderDeliveryFee = deliveryFees.stream()
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        return productTotal.add(orderDeliveryFee);
    }

    @Override
    public List<SaleDtoResponse> getSalesByUserNameAndDeliveryStatus(String userName, DeliveryStatus deliveryStatus) {
        User user = userService.findUserName(userName);
        return saleMapper.salesToSaleDtos(saleRepo.findByUserIdAndDeliveryStatus(user.getId(),deliveryStatus));
    }

    @Override
    public List<SaleDtoResponse> getAllSale() {

       return saleMapper.salesToSaleDtos(saleRepo.findAll());
    }

    // --- Helper methods ---

    private Map<Long, ProductVariant> fetchVariants(SaleDto saleDto) {
        List<Long> variantIds = saleDto.getSaleDetailDTOS().stream()
                .map(SaleDetailDTO::getVariantId)
                .filter(Objects::nonNull)
                .toList();
        return variantRepo.findAllById(variantIds).stream()
                .collect(Collectors.toMap(ProductVariant::getId, Function.identity()));
    }

    private ProcessingResult processItems(SaleDto saleDto, Map<Long, ProductVariant> variantMap) {
        List<Stock> stocksToUpdate = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalQty = 0;
        String numberPhone = saleDto.getPhoneNumber();
        String address = saleDto.getAddress();
        double latitude = saleDto.getLatitude();
        double longitude = saleDto.getLongitude();
        List<SaleDetail> saleDetails = new ArrayList<>();

        // Collect delivery fees from all products in the order
        Set<BigDecimal> deliveryFees = new HashSet<>();

        for (SaleDetailDTO detail : saleDto.getSaleDetailDTOS()) {
            ProductVariant variant = getVariantOrThrow(detail, variantMap);

            if (detail.getColorId() != null && variant.getColor() != null &&
                    !detail.getColorId().equals(variant.getColor().getId())) {
                throw new RuntimeException("Color mismatch: variant colorId=" + variant.getColor().getId() +
                        ", but requested colorId=" + detail.getColorId());
            }

            Stock stock = getStockOrThrow(detail, variant);
            validateStockQuantity(stock, detail.getQuantity());

            BigDecimal itemPrice = variant.getProduct().getFinalPrice()
                    .multiply(BigDecimal.valueOf(detail.getQuantity()));
            totalPrice = totalPrice.add(itemPrice);
            totalQty += detail.getQuantity();

            // Collect delivery fee for this product if present
            BigDecimal productDeliveryFee = variant.getProduct().getDeliveryFee();
            if (productDeliveryFee != null && productDeliveryFee.compareTo(BigDecimal.ZERO) > 0) {
                deliveryFees.add(productDeliveryFee);
            }

            saleDetails.add(createSaleDetail(variant, stock, detail));
        }

        // Decide on the delivery fee for the whole order
        // Example: Use the highest delivery fee among all products, or a fixed value
        BigDecimal orderDeliveryFee = deliveryFees.stream()
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        totalPrice = totalPrice.add(orderDeliveryFee);

        // Optionally, you can return the delivery fee as part of the ProcessingResult
        return new ProcessingResult(
                stocksToUpdate,
                totalPrice,
                totalQty,
                saleDetails,
                numberPhone,
                address,
                latitude,
                longitude,
                orderDeliveryFee // add this to your ProcessingResult if needed
        );
    }


    private ProductVariant getVariantOrThrow(SaleDetailDTO detail, Map<Long, ProductVariant> variantMap) {
        return Optional.ofNullable(variantMap.get(detail.getVariantId()))
                .orElseThrow(() -> new ResoureApiNotFound("ProductVariant not found", detail.getVariantId()));
    }

    private Stock getStockOrThrow(SaleDetailDTO detail, ProductVariant variant) {
        return variant.getStocks().stream()
                .filter(s -> s.getSize().getId().equals(detail.getSizeId()))
                .findFirst()
                .orElseThrow(() -> new ResoureApiNotFound("Size not found for product variant", detail.getSizeId()));
    }

    private void validateStockQuantity(Stock stock, int requestedQty) {
        if (stock.getCurrentQuantity() < requestedQty) {
            throw new RuntimeException("Insufficient stock: " + stock.getCurrentQuantity() +
                    " left, requested: " + requestedQty);
        }
    }

    private SaleDetail createSaleDetail(ProductVariant variant, Stock stock, SaleDetailDTO detail) {
        SaleDetail saleDetail = new SaleDetail();
        saleDetail.setProduct(variant.getProduct());
        saleDetail.setProductVariant(variant);
        saleDetail.setSize(stock.getSize());
        saleDetail.setQuantity(detail.getQuantity());
        saleDetail.setFinalPrice(variant.getProduct().getFinalPrice().doubleValue());
        saleDetail.setDiscount(variant.getProduct().getDiscount().doubleValue());
        saleDetail.setPrice(variant.getProduct().getPrice().doubleValue());
        saleDetail.setColor(variant.getColor());
        return saleDetail;
    }

    private Sale createSaleRecord(ProcessingResult result, User user) {
        Sale sale = new Sale();
        sale.setStatus("PAID");
        sale.setUser(user);
        sale.setFinalPrice(result.totalPrice());
        sale.setQuantity(result.totalQty());
        sale.setPhoneNumber(result.numberPhone);
        sale.setAddress(result.address);
        sale.setLatitude(result.latitude);
        sale.setLongitude(result.longitude);
        sale.setDeliveryStatus(DeliveryStatus.PENDING);
        return sale;
    }

    private void saveSaleDetails(List<SaleDetail> saleDetails, Sale savedSale) {
        saleDetails.forEach(detail -> detail.setSale(savedSale));
        saleDetailRepo.saveAll(saleDetails);
    }

    // --- Record for processing results ---
    private record ProcessingResult(
            List<Stock> stocksToUpdate,
            BigDecimal totalPrice,
            int totalQty,
            List<SaleDetail> saleDetails,
            String numberPhone,
            String address,
            double latitude,
            double longitude,
            BigDecimal orderDeliveryFee) {}
}
