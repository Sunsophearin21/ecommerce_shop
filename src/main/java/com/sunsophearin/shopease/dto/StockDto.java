package com.sunsophearin.shopease.dto;

import lombok.Data;

@Data
public class StockDto {
    private Long productVariantId;
    private Long sizeId;
    private Integer quantity;
}
