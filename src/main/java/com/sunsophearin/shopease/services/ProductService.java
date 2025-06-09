package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.ProductDto;
import com.sunsophearin.shopease.dto.ProductDtoRespone;
import com.sunsophearin.shopease.dto.ProductVariantDto;
import com.sunsophearin.shopease.entities.Product;
import com.sunsophearin.shopease.entities.ProductVariant;
import com.sunsophearin.shopease.specification.ProductFilter;
import com.sunsophearin.shopease.specification.productFilter2;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductService {
    Product createProduct(ProductDto dto);
    List<Product> getProducts();
    Product getProductById(Long id);
    List<Product> searchProducts(productFilter2 filter);
    List<Product> getProductsByVariant(Long productId,List<Long> variantIds);
    Product findVariant(Long productId, List<Long> colorIds);
}
