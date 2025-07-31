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
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final ProductVariantRepository variantRepository;
    private final StockRepository stockRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final UserServiceImpl userService;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final SaleMapper saleMapper;

    /**
     * Enrich SaleDetailDTO list by fetching names and prices from related entities in batch.
     *
     * @param saleDto the sale data transfer object to enrich
     */
    @Override
    public void enrichSaleDetailDTOs(final SaleDto saleDto) {
        if (saleDto == null || saleDto.getSaleDetailDTOS() == null) {
            return;
        }

        final List<SaleDetailDTO> details = saleDto.getSaleDetailDTOS();

        // Batch fetch products, colors, sizes
        List<Long> productIds = details.stream()
                .map(SaleDetailDTO::getProductId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<Long> colorIds = details.stream()
                .map(SaleDetailDTO::getColorId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, Color> colorMap = colorRepository.findAllById(colorIds).stream()
                .collect(Collectors.toMap(Color::getId, Function.identity()));

        List<Long> sizeIds = details.stream()
                .map(SaleDetailDTO::getSizeId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, Size> sizeMap = sizeRepository.findAllById(sizeIds).stream()
                .collect(Collectors.toMap(Size::getId, Function.identity()));

        for (SaleDetailDTO detail : details) {
            Product product = productMap.get(detail.getProductId());
            if (product == null) {
                throw new RuntimeException("Product not found with ID: " + detail.getProductId());
            }
            detail.setProductName(product.getName());
            detail.setUnitPrice(product.getFinalPrice());

            Color color = colorMap.get(detail.getColorId());
            detail.setColorName(color != null ? color.getName() : null);

            Size size = sizeMap.get(detail.getSizeId());
            detail.setSizeName(size != null ? size.getName() : null);
        }
    }

    /**
     * Processes a sale: validates stock, applies possible first-purchase discount,
     * updates stock and saves the sale and its details within a transaction.
     *
     * @param saleDto   the sale DTO containing order details
     * @param userEmail the email of the user making the purchase
     * @return the saved Sale entity
     */
    @Override
    @Transactional
    public Sale processSale(final SaleDto saleDto, final String userEmail) {
        Objects.requireNonNull(saleDto, "SaleDto must not be null");
        Objects.requireNonNull(userEmail, "User email must not be null");

        enrichSaleDetailDTOs(saleDto);

        final User user = userService.findUserName(userEmail);
        final boolean isFirstPurchase = user.isFirstPurchase();

        final Map<Long, ProductVariant> variantMap = fetchVariants(saleDto);

        final ProcessingResult procResult = processSaleItems(saleDto, variantMap);

        // Update stock quantities for each item, validate again before save
        for (SaleDetailDTO detail : saleDto.getSaleDetailDTOS()) {
            ProductVariant variant = variantMap.get(detail.getVariantId());
            Stock stock = getStockOrThrow(detail, variant);

            final int newQuantity = stock.getCurrentQuantity() - detail.getQuantity();
            if (newQuantity < 0) {
                throw new RuntimeException(String.format(
                        "Insufficient stock for variantId=%d, sizeId=%d",
                        variant.getId(), detail.getSizeId()));
            }
            stock.setCurrentQuantity(newQuantity);
            stockRepository.save(stock);
        }
        // Although procResult.stocksToUpdate is empty because stocks saved above,
        // keep this line in case future changes populate it.
        stockRepository.saveAll(procResult.stocksToUpdate());

        // Apply first purchase discount of 10%
        BigDecimal saleFinalPrice = procResult.totalPrice();
        if (isFirstPurchase) {
            saleFinalPrice = saleFinalPrice.multiply(BigDecimal.valueOf(0.9))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        Sale sale = createSaleRecord(saleFinalPrice, procResult.totalQty(), user, procResult);

        // Generate unique transaction ID if not set
        if (sale.getTransactionId() == null || sale.getTransactionId().isEmpty()) {
            sale.setTransactionId("tst" + UUID.randomUUID().toString().replace("-", ""));
        }

        final Sale savedSale = saleRepository.save(sale);
        saveSaleDetails(procResult.saleDetails(), savedSale);

        if (isFirstPurchase) {
            user.setFirstPurchase(false);
            userService.saveUser(user);
        }

        return savedSale;
    }

    /**
     * Calculate total price of sale items without discount or delivery fees.
     *
     * @param saleDto the sale DTO containing sale details
     * @return total price as BigDecimal rounded to 2 decimals
     */
    @Override
    public BigDecimal calculateTotalPrice(final SaleDto saleDto) {
        if (saleDto == null || saleDto.getSaleDetailDTOS() == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        enrichSaleDetailDTOs(saleDto);

        return saleDto.getSaleDetailDTOS().stream()
                .filter(d -> d.getUnitPrice() != null && d.getQuantity() != null)
                .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate total price including delivery fees, applying 10% discount if user eligible.
     *
     * @param saleDto the sale DTO
     * @param user    the user making the purchase (optional, may be null)
     * @return total price with delivery and discount if applicable
     */
    @Override
    public BigDecimal calculateTotalPriceWithDelivery(final SaleDto saleDto, final User user) {
        if (saleDto == null || saleDto.getSaleDetailDTOS() == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        final Map<Long, ProductVariant> variantMap = fetchVariants(saleDto);
        // This wrapper validates input consistency; can skip if confident inputs are valid
        processSaleItems(saleDto, variantMap);

        final BigDecimal productTotal = calculateTotalPrice(saleDto);

        final BigDecimal maxDeliveryFee = saleDto.getSaleDetailDTOS().stream()
                .map(d -> productRepository.findById(d.getProductId())
                        .map(Product::getDeliveryFee)
                        .orElse(BigDecimal.ZERO))
                .filter(fee -> fee.compareTo(BigDecimal.ZERO) > 0)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal discountedTotal = productTotal;

        if (user != null && user.isFirstPurchase()) {
            discountedTotal = productTotal.multiply(BigDecimal.valueOf(0.9)).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal total = discountedTotal.add(maxDeliveryFee);
        return total;

    }

    @Override
    public List<SaleDtoResponse> getSalesByUserNameAndDeliveryStatus(final String userName, final DeliveryStatus deliveryStatus) {
        final User user = userService.findUserName(userName);
        return saleMapper.salesToSaleDtos(saleRepository.findByUserIdAndDeliveryStatus(user.getId(), deliveryStatus));
    }

    @Override
    public Map<DeliveryStatus, List<SaleDtoResponse>> getSalesGroupedByStatus(final String email) {
        final User user = userService.findUserName(email);
        List<Sale> allSales = saleRepository.findByUserId(user.getId());
        return allSales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getDeliveryStatus,
                        Collectors.mapping(saleMapper::saleToSaleDto, Collectors.toList())
                ));
    }

    @Override
    public List<SaleDtoResponse> getAllSale() {
        return saleMapper.salesToSaleDtos(saleRepository.findAll());
    }

    // -- Helper private methods --

    /**
     * Fetch all product variants in the sale DTO by their IDs.
     */
    private Map<Long, ProductVariant> fetchVariants(final SaleDto saleDto) {
        List<Long> variantIds = saleDto.getSaleDetailDTOS().stream()
                .map(SaleDetailDTO::getVariantId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        return variantRepository.findAllById(variantIds).stream()
                .collect(Collectors.toMap(ProductVariant::getId, Function.identity()));
    }

    /**
     * Validates stock availability, calculates totals, and prepares sale details.
     *
     * @param saleDto    sale data transfer object
     * @param variantMap product variant map for quick lookup
     * @return processing results with totals and sale details
     */
    private ProcessingResult processSaleItems(final SaleDto saleDto,
                                              final Map<Long, ProductVariant> variantMap) {

        final List<Stock> stocksToUpdate = new ArrayList<>();  // kept for future enhancement if needed
        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalQty = 0;
        final String phoneNumber = saleDto.getPhoneNumber();
        final String address = saleDto.getAddress();
        final double latitude = saleDto.getLatitude();
        final double longitude = saleDto.getLongitude();

        final List<SaleDetail> saleDetails = new ArrayList<>();
        final Set<BigDecimal> deliveryFees = new HashSet<>();

        for (SaleDetailDTO detail : saleDto.getSaleDetailDTOS()) {
            ProductVariant variant = getVariantOrThrow(detail, variantMap);

            if (detail.getColorId() != null && variant.getColor() != null &&
                    !detail.getColorId().equals(variant.getColor().getId())) {
                throw new RuntimeException(
                        "Color mismatch for variant ID: " + variant.getId()
                                + " requested colorId: " + detail.getColorId());
            }

            Stock stock = getStockOrThrow(detail, variant);
            validateStockQuantity(stock, detail.getQuantity());

            BigDecimal itemTotal = variant.getProduct().getFinalPrice()
                    .multiply(BigDecimal.valueOf(detail.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
            totalQty += detail.getQuantity();

            BigDecimal deliveryFee = variant.getProduct().getDeliveryFee();
            if (deliveryFee != null && deliveryFee.compareTo(BigDecimal.ZERO) > 0) {
                deliveryFees.add(deliveryFee);
            }

            saleDetails.add(createSaleDetail(variant, stock, detail));
        }

        final BigDecimal maxDeliveryFee = deliveryFees.stream()
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        totalPrice = totalPrice.add(maxDeliveryFee);

        return new ProcessingResult(
                stocksToUpdate,
                totalPrice,
                totalQty,
                saleDetails,
                phoneNumber,
                address,
                latitude,
                longitude,
                maxDeliveryFee
        );
    }

    private ProductVariant getVariantOrThrow(final SaleDetailDTO detail, final Map<Long, ProductVariant> variantMap) {
        return Optional.ofNullable(variantMap.get(detail.getVariantId()))
                .orElseThrow(() -> new ResoureApiNotFound("ProductVariant not found", detail.getVariantId()));
    }

    private Stock getStockOrThrow(final SaleDetailDTO detail, final ProductVariant variant) {
        return variant.getStocks().stream()
                .filter(stock -> stock.getSize().getId().equals(detail.getSizeId()))
                .findFirst()
                .orElseThrow(() -> new ResoureApiNotFound("Size not found for variant", detail.getSizeId()));
    }

    private void validateStockQuantity(final Stock stock, final int requestedQty) {
        if (stock.getCurrentQuantity() < requestedQty) {
            throw new RuntimeException(String.format(
                    "Insufficient stock: %d left, but %d requested",
                    stock.getCurrentQuantity(), requestedQty));
        }
    }

    private SaleDetail createSaleDetail(final ProductVariant variant,
                                        final Stock stock,
                                        final SaleDetailDTO detail) {
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

    private Sale createSaleRecord(final BigDecimal finalPrice,
                                  final int totalQty,
                                  final User user,
                                  final ProcessingResult result) {
        Sale sale = new Sale();
        sale.setStatus("PAID");
        sale.setUser(user);
        sale.setFinalPrice(finalPrice);
        sale.setQuantity(totalQty);
        sale.setPhoneNumber(result.numberPhone());
        sale.setAddress(result.address());
        sale.setLatitude(result.latitude());
        sale.setLongitude(result.longitude());
        sale.setDeliveryStatus(DeliveryStatus.PENDING);
        return sale;
    }

    private void saveSaleDetails(final List<SaleDetail> saleDetails, final Sale savedSale) {
        saleDetails.forEach(detail -> detail.setSale(savedSale));
        saleDetailRepository.saveAll(saleDetails);
    }

    /**
     * Container for internal processing results.
     */
    private record ProcessingResult(
            List<Stock> stocksToUpdate,
            BigDecimal totalPrice,
            int totalQty,
            List<SaleDetail> saleDetails,
            String numberPhone,
            String address,
            double latitude,
            double longitude,
            BigDecimal orderDeliveryFee
    ) {
    }
}
