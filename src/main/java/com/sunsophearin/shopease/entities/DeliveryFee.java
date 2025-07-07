package com.sunsophearin.shopease.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Entity
@Table(name = "delivery_fees")
public class DeliveryFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image")
    private String image; // URL or filename

    @Column(name = "name", nullable = false)
    private String name; // Delivery service name

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price; // Delivery fee

    @Column(name = "description")
    private String description; // Details about the delivery

    @Column(name = "company")
    private String company; // Delivery company name

    @Column(name = "delivery")
    private String delivery; // Delivery type/method (e.g., standard, express)
}

