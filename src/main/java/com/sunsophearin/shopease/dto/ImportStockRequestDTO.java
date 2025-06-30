package com.sunsophearin.shopease.dto;

import lombok.Data;

@Data
public class ImportStockRequestDTO {
    private Long productVariantId;
    private Long sizeId;
    private int quantity;
    private double importPricePerUnit;
}
