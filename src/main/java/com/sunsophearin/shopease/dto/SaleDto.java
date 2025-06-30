package com.sunsophearin.shopease.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SaleDto {
    private String transactionId;
//    private String userEmail;
    private List<SaleDetailDTO> saleDetailDTOS;
//    private double totalAmount;

    // Generate transaction ID if not set
    public SaleDto() {
        if (this.transactionId == null) {
            this.transactionId = "tst" + UUID.randomUUID().toString().replace("-", "");
        }
    }

//    public double getTotalAmount() {
//        return saleDetailDTOS.stream()
//                .mapToDouble(detail -> detail.get() * detail.getQuantity())
//                .sum();
//    }

    // Getters and setters
}
