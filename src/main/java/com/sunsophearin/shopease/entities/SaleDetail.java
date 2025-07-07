package com.sunsophearin.shopease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Product product;
    @ManyToOne
    @JoinColumn(name = "productVariant_id")
    @JsonIgnore
    private ProductVariant productVariant;
    @ManyToOne
    @JoinColumn(name = "size_id")
    @JsonIgnore
    private Size size;
    @ManyToOne
    @JoinColumn(name = "color_id")
    @JsonIgnore
    private Color color;
}
