package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.ProductVariantDto;
import com.sunsophearin.shopease.entities.ProductVariant;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductVariantService {
//    ProductVariant createProductVariant(ProductVariantDto dto);
    ProductVariant getById(Long id);
    ProductVariant update(Long id, ProductVariantDto dto);
    ProductVariant createProductVariant(ProductVariantDto dto, MultipartFile[] files) throws IOException;

}
