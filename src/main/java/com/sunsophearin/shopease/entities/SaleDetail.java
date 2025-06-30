package com.sunsophearin.shopease.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class SaleDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    private Double price;
    private Double discount;
    private Double finalPrice;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "productVariant_id")
    private ProductVariant productVariant;
    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;
    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;
}
