package com.sunsophearin.shopease.services.impl;

import com.sunsophearin.shopease.dto.SaleDetailDTO;
import com.sunsophearin.shopease.dto.SaleDto;
import com.sunsophearin.shopease.entities.*;
import com.sunsophearin.shopease.exception.ResoureApiNotFound;
import com.sunsophearin.shopease.repositories.*;
import com.sunsophearin.shopease.security.entities.User;
import com.sunsophearin.shopease.security.repository.UserRepository;
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

    @Override
    @Transactional
    public void processSale(SaleDto saleDto, String userEmail) {
        User userName = userService.findUserName(userEmail);
        // 1. Fetch products and variants in batch
        Map<Long, Product> productMap = fetchProducts(saleDto);
        Map<Long, ProductVariant> variantMap = fetchVariants(saleDto);

        // 2. Prepare data for updates
        ProcessingResult result = processItems(saleDto, variantMap);
//        BigDecimal totalPrice = result.totalPrice;
        for (SaleDetailDTO detail : saleDto.getSaleDetailDTOS()) {
            ProductVariant variant = variantMap.get(detail.getVariantId());
            Stock stock = getStockOrThrow(detail, variant);
            stock.setCurrentQuantity(stock.getCurrentQuantity() - detail.getQuantity());
            stockRepo.save(stock);
        }
        // 3. Update stock
        stockRepo.saveAll(result.stocksToUpdate());

        // 4. Create and save sale record
        Sale sale = createSaleRecord(result,userName);
        Sale savedSale = saleRepo.save(sale);

        // 5. Save sale details
        saveSaleDetails(result.saleDetails(), savedSale);
    }

    @Override
    public BigDecimal calculateTotalPrice(SaleDto saleDto) {
        Map<Long, ProductVariant> variantMap = fetchVariants(saleDto);
        return processItems(saleDto, variantMap).totalPrice();
    }

    // Helper: Fetch products as a map
    private Map<Long, Product> fetchProducts(SaleDto saleDto) {
        List<Long> productIds = saleDto.getSaleDetailDTOS().stream()
                .map(SaleDetailDTO::getProductId)
                .toList();

        return productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    // Helper: Fetch variants as a map
    private Map<Long, ProductVariant> fetchVariants(SaleDto saleDto) {
        List<Long> variantIds = saleDto.getSaleDetailDTOS().stream()
                .map(SaleDetailDTO::getVariantId)
                .toList();

        return variantRepo.findAllById(variantIds).stream()
                .collect(Collectors.toMap(ProductVariant::getId, Function.identity()));
    }

    // Helper: Process each sale item
    private ProcessingResult processItems(SaleDto saleDto, Map<Long, ProductVariant> variantMap) {
        List<Stock> stocksToUpdate = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalQty = 0;
        List<SaleDetail> saleDetails = new ArrayList<>();

        for (SaleDetailDTO detail : saleDto.getSaleDetailDTOS()) {
            ProductVariant variant = getVariantOrThrow(detail, variantMap);

            // --- Color validation ---
            if (detail.getColorId() != null && variant.getColor() != null &&
                    !detail.getColorId().equals(variant.getColor().getId())) {
                throw new RuntimeException(
                        "Color mismatch: variant colorId=" + variant.getColor().getId() +
                                ", but requested colorId=" + detail.getColorId()
                );
            }
            // ------------------------

            Stock stock = getStockOrThrow(detail, variant);
            validateStockQuantity(stock, detail.getQuantity());

//            updateStock(stock, detail.getQuantity(), stocksToUpdate);
            ProcessingResult itemResult = calculateItem(variant, detail.getQuantity());

            totalPrice = totalPrice.add(itemResult.totalPrice);
            totalQty += detail.getQuantity();
            saleDetails.add(createSaleDetail(variant, stock, detail));
        }


        return new ProcessingResult(stocksToUpdate, totalPrice, totalQty, saleDetails);
    }

    // Helper: Get variant or throw error
    private ProductVariant getVariantOrThrow(SaleDetailDTO detail, Map<Long, ProductVariant> variantMap) {
        return Optional.ofNullable(variantMap.get(detail.getVariantId()))
                .orElseThrow(() -> new ResoureApiNotFound(
                        "ProductVariant not found",
                        detail.getVariantId()
                ));
    }

    // Helper: Get stock or throw error
    private Stock getStockOrThrow(SaleDetailDTO detail, ProductVariant variant) {
        return variant.getStocks().stream()
                .filter(s -> s.getSize().getId().equals(detail.getSizeId()))
                .findFirst()
                .orElseThrow(() -> new ResoureApiNotFound(
                        "Size not found for product variant",
                        detail.getSizeId()
                ));
    }

    // Helper: Validate stock quantity
    private void validateStockQuantity(Stock stock, int requestedQty) {
        if (stock.getCurrentQuantity() < requestedQty) {
            throw new RuntimeException(
                    "Insufficient stock: " + stock.getCurrentQuantity() +
                            " left, requested: " + requestedQty
            );
        }
    }

    // Helper: Update stock
    private void updateStock(Stock stock, int soldQty, List<Stock> stocksToUpdate) {
        stock.setCurrentQuantity(stock.getCurrentQuantity() - soldQty);
        stocksToUpdate.add(stock);
    }

    // Helper: Calculate item price
    private ProcessingResult calculateItem(ProductVariant variant, int quantity) {
        BigDecimal itemPrice = variant.getProduct().getFinalPrice()
                .multiply(BigDecimal.valueOf(quantity));
        return new ProcessingResult(itemPrice, quantity);
    }

    // Helper: Create sale detail
    private SaleDetail createSaleDetail(ProductVariant variant, Stock stock, SaleDetailDTO detail) {
        SaleDetail saleDetail = new SaleDetail();
        saleDetail.setProduct(variant.getProduct());
        saleDetail.setProductVariant(variant);
        saleDetail.setSize(stock.getSize());
        saleDetail.setQuantity(detail.getQuantity());
        saleDetail.setFinalPrice(variant.getProduct().getFinalPrice().doubleValue());
        return saleDetail;
    }

    // Helper: Create sale record
    private Sale createSaleRecord(ProcessingResult result,User user) {
        Sale sale = new Sale();
        sale.setStatus("PAID");
        sale.setUser(user);
        sale.setFinalPrice(result.totalPrice());
        sale.setQuantity(result.totalQty());
        return sale;
    }

    // Helper: Save sale details
    private void saveSaleDetails(List<SaleDetail> saleDetails, Sale savedSale) {
        saleDetails.forEach(detail -> detail.setSale(savedSale));
        saleDetailRepo.saveAll(saleDetails);
    }

    // Record class for processing results
    private record ProcessingResult(
            List<Stock> stocksToUpdate,
            BigDecimal totalPrice,
            int totalQty,
            List<SaleDetail> saleDetails
    ) {
        // Overloaded constructor for item calculation
        public ProcessingResult(BigDecimal itemPrice, int quantity) {
            this(null, itemPrice, quantity, null);
        }
    }
}
