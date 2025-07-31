package com.sunsophearin.shopease.services.impl;

import com.sunsophearin.shopease.dto.ImportStockRequestDTO;
import com.sunsophearin.shopease.dto.ProductDto;
import com.sunsophearin.shopease.dto.response.ProductDtoRespone;
import com.sunsophearin.shopease.entities.*;
import com.sunsophearin.shopease.exception.ResoureApiNotFound;
import com.sunsophearin.shopease.mapper.ProductMapper;
import com.sunsophearin.shopease.repositories.*;
import com.sunsophearin.shopease.services.ProductService;
import com.sunsophearin.shopease.services.ProductVariantService;
import com.sunsophearin.shopease.services.SizeService;
import com.sunsophearin.shopease.specification.ProductSpec;
import com.sunsophearin.shopease.specification.productFilter2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ImportStockRepository importStockRepository;
    private final StockRepository stockRepository;
    private final ProductVariantService productVariantService;
    private final SizeService sizeService;
    @Override
    public Product createProduct(ProductDto dto) {
        return productRepository.save(productMapper.productDtoToProduct(dto));
    }

    @Override
    public List<ProductDtoRespone> getProducts() {
        return productRepository.findAll().stream().map(productMapper::toDtoList).toList();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new ResoureApiNotFound("Product",id));
    }

    @Override
    public List<Product> searchProducts(productFilter2 filter) {
        Specification<Product> spec = new ProductSpec(filter);

        return productRepository.findAll(spec);
    }

    @Override
    public List<Product> getProductsByVariant(Long productId, List<Long> variantIds) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<ProductVariant> variants = product.getProductVariants();

        if (variantIds != null && !variantIds.isEmpty()) {
            variants = variants.stream()
                    .filter(v -> variantIds.contains(v.getId()))
                    .peek(v -> {
                        // Force loading color if it's lazily loaded
                        if (v.getColor() != null) {
                            v.getColor().getName(); // triggers lazy load
                        }
                    })
                    .toList();
        }

        product.setProductVariants(variants);
        return List.of(product);
    }

    @Override
    public ProductDtoRespone findVariant(Long productId, Long color_id,Long size_id) {
        Product productById = getProductById(productId);

        List<ProductVariant> variants = productById.getProductVariants();
        if (variants == null || variants.isEmpty()) {
            return null;
        }

        // Step 1: Filter by color_id if provided
        List<ProductVariant> filteredVariants = variants;
        if (color_id != null) {
            filteredVariants = variants.stream()
                    .filter(v -> v.getColor() != null && color_id.equals(v.getColor().getId()))
                    .collect(Collectors.toList());
        }

        // Step 2: If size_id is provided, filter importProducts
        if (size_id != null) {
            for (ProductVariant variant : filteredVariants) {
                List<Stock> filteredImports = variant.getStocks().stream()
                        .filter(ip -> ip.getSize() != null && size_id.equals(ip.getSize().getId()))
                        .collect(Collectors.toList());

                variant.setStocks(filteredImports);
            }
        }

        // Step 3: Update the product with filtered variants
        productById.setProductVariants(filteredVariants);

        return productMapper.toDtoList(productById);
    }


    @Override
    public List<Product> getProductByCategoryType(Long cateId) {
        List<Product> products = productRepository.findByCategoryTypeId(cateId);
        if (products.isEmpty()) {
            throw new ResoureApiNotFound("Product with category type", cateId);
        }
        return products;
    }

    @Override
    public List<Product> getProductByCategoryItem(Long categoryItemId) {
        List<Product> products = productRepository.findByCategoryItemId(categoryItemId);
        if (products.isEmpty()) {
            throw new ResoureApiNotFound("Product with category type", categoryItemId);
        }
        return products;
    }

    @Transactional
    @Override
    public ImportStock importProduct(ImportStockRequestDTO dto) {
        // 1. Validate input
        validateImportRequest(dto);

        // 2. Fetch required entities
        ProductVariant variant = productVariantService.getById(dto.getProductVariantId());
        Size size = sizeService.getSizeById(dto.getSizeId());

        // 3. Create import record
        ImportStock savedImport = createImportRecord(dto, variant, size);

        // 4. Update stock
        updateStockInventory(dto, variant, size);

        return savedImport;
    }

    private void validateImportRequest(ImportStockRequestDTO dto) {
        Assert.notNull(dto, "Import request DTO cannot be null");
        Assert.notNull(dto.getProductVariantId(), "Product variant ID is required");
        Assert.notNull(dto.getSizeId(), "Size ID is required");
        Assert.isTrue(dto.getQuantity() > 0, "Quantity must be positive");
        Assert.isTrue(dto.getImportPricePerUnit() > 0, "Import price must be positive");
    }

    private ImportStock createImportRecord(ImportStockRequestDTO dto, ProductVariant variant, Size size) {
        return importStockRepository.save(
                ImportStock.builder()
                        .productVariant(variant)
                        .size(size)
                        .quantity(dto.getQuantity())
                        .importPricePerUnit(dto.getImportPricePerUnit())
                        .build()
        );
    }

    private void updateStockInventory(ImportStockRequestDTO dto, ProductVariant variant, Size size) {
        stockRepository.findByProductVariantAndSize(variant, size)
                .ifPresentOrElse(
                        stock -> updateExistingStock(stock, dto),
                        () -> createNewStock(variant, size, dto)
                );
    }

    private void updateExistingStock(Stock stock, ImportStockRequestDTO dto) {
        int totalQty = stock.getCurrentQuantity() + dto.getQuantity();
        double newAveragePrice = calculateWeightedAverage(
                stock.getCurrentQuantity(),
                stock.getAverageImportPrice(),
                dto.getQuantity(),
                dto.getImportPricePerUnit()
        );

        stock.setCurrentQuantity(totalQty);
        stock.setAverageImportPrice(newAveragePrice);
        stockRepository.save(stock);
    }

    private double calculateWeightedAverage(int currentQty, double currentPrice, int newQty, double newPrice) {
        double currentTotal = currentPrice * currentQty;
        double newTotal = newPrice * newQty;
        return (currentTotal + newTotal) / (currentQty + newQty);
    }

    private void createNewStock(ProductVariant variant, Size size, ImportStockRequestDTO dto) {
        stockRepository.save(
                Stock.builder()
                        .productVariant(variant)
                        .size(size)
                        .currentQuantity(dto.getQuantity())
                        .averageImportPrice(dto.getImportPricePerUnit())
                        .build()
        );
    }
    @Override
    public Stock getStockById(Long id) {
        return stockRepository.findById(id).orElseThrow(()->new ResoureApiNotFound("Stock",id));
    }
}
