package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.ImportStockRequestDTO;
import com.sunsophearin.shopease.dto.ProductDto;
import com.sunsophearin.shopease.dto.ProductDtoRespone;
import com.sunsophearin.shopease.entities.ImportStock;
import com.sunsophearin.shopease.entities.Product;
import com.sunsophearin.shopease.entities.Stock;
import com.sunsophearin.shopease.specification.productFilter2;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductDto dto);
    List<Product> getProducts();
    Product getProductById(Long id);
    List<Product> searchProducts(productFilter2 filter);
    List<Product> getProductsByVariant(Long productId,List<Long> variantIds);
    ProductDtoRespone findVariant(Long productId, Long color_id,Long size_id);
    List<Product> getProductByCategoryType(Long cateId);
    ImportStock importProduct(ImportStockRequestDTO dto);
    Stock getStockById(Long id);
}
