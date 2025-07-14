package com.sunsophearin.shopease.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class SaleDetailDtoResponse {
    private Long id;
    private Double price;
    private Double discount;
    private Double finalPrice;
    private Integer quantity;
    private String productName;
    private String productVariantName;
    private String sizeName;
    private String colorName;
    private List<String> images;
    // getters and setters
}