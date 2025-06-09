package com.sunsophearin.shopease.services.impl;

import com.sunsophearin.shopease.dto.ProductDto;
import com.sunsophearin.shopease.dto.ProductDtoRespone;
import com.sunsophearin.shopease.dto.ProductVariantDto;
import com.sunsophearin.shopease.entities.Product;
import com.sunsophearin.shopease.entities.ProductVariant;
import com.sunsophearin.shopease.exception.ApiNotFoundException;
import com.sunsophearin.shopease.exception.ResoureApiNotFound;
import com.sunsophearin.shopease.mapper.ProductMapper;
import com.sunsophearin.shopease.mapper.ProductVariantMapper;
import com.sunsophearin.shopease.repositories.ProductRepository;
import com.sunsophearin.shopease.repositories.ProductVariantRepository;
import com.sunsophearin.shopease.services.ProductService;
import com.sunsophearin.shopease.services.UploadImageFileService;
import com.sunsophearin.shopease.specification.ProductFilter;
import com.sunsophearin.shopease.specification.ProductSpec;
import com.sunsophearin.shopease.specification.ProductSpecification;
import com.sunsophearin.shopease.specification.productFilter2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductVariantRepository productVariantRepository;
    @Override
    public Product createProduct(ProductDto dto) {
        return productRepository.save(productMapper.productDtoToProduct(dto));
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
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
    public Product findVariant(Long productId,  List<Long> colorIds) {
        Product productById = getProductById(productId);
        List<ProductVariant> variants = productById.getProductVariants();
        if (colorIds != null && !colorIds.isEmpty()) {
            variants = variants.stream()
                    .filter(v -> colorIds.contains(v.getColor().getId()))
                    .toList();
        }

        productById.setProductVariants(variants); // update the product with filtered variants

        return productById;
    }
}
