package com.sunsophearin.shopease.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private double amount;
    private String transactionId;
    private String email;
    private String username;
    private Long productId;
    private Long productVariantId;
    private Long sizeId;
    private int quantity;
    // + add profile if needed
}