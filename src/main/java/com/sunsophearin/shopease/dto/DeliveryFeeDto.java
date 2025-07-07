package com.sunsophearin.shopease.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeliveryFeeDto {
    private String image;        // URL or filename of the delivery image/logo
    private String name;         // Delivery option name
    private BigDecimal price;    // Delivery fee amount
    private String description;  // Description of delivery option
    private String company;      // Name of the delivery company
    private String delivery;
}
