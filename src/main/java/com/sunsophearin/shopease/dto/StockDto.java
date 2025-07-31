package com.sunsophearin.shopease.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockDto {
    private Long id;
    private int currentQuantity;
    private BigDecimal averageImportPrice;
    private SizeDto size;
    private Long productVariantId; // ✅ បន្ថែមនៅទីនេះ
}

