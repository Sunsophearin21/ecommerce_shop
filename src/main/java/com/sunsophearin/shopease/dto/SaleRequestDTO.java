package com.sunsophearin.shopease.dto;

import lombok.Data;

@Data
public class SaleRequestDTO {
    private Long productId;
    private Long productVariantId;
    private Long sizeId;
    private int quantity;
}
