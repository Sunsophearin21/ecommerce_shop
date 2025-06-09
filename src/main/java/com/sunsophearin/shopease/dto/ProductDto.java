package com.sunsophearin.shopease.dto;

import com.sunsophearin.shopease.entities.ProductVariant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long categoryId;
    private Long categoryTypeId;
    private Long menuFacturerId;
    private List<ProductVariantDto> productVariantdto;
    private String name;
    private String description;
    private BigDecimal price;
    private  boolean isNewArrival;
}
