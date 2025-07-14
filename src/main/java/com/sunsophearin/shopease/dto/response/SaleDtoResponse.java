package com.sunsophearin.shopease.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class SaleDtoResponse {
    private Long id;
    private String orderId;
    private String transactionId;
    private String status;
    private String deliveryStatus;
    private String notes;
    private int quantity;
    private BigDecimal finalPrice;
    private String phoneNumber;
    private String address;
    private double latitude;
    private double longitude;
    private Date createAt;
    private Date updateAt;
    private String userName;
    private List<SaleDetailDtoResponse> saleDetails;
    // getters and setters
}
