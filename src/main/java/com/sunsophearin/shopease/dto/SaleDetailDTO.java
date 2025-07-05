package com.sunsophearin.shopease.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SaleDetailDTO {
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private Long variantId;
    private Long colorId;
    private String colorName; // Add this
    private Long sizeId;
    private String sizeName;  // Add this
    private Integer quantity;
}

