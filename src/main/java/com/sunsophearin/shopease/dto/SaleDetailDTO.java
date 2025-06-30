package com.sunsophearin.shopease.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class SaleDetailDTO {
    private Long productId;
    private Long variantId;
    private Long colorId;
    private Long sizeId;
    private Integer quantity;
}
