package com.sunsophearin.shopease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Stock {
    @Id
    @GeneratedValue
    private Long id;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "size_id", nullable = false)
    private Size size;
    @ManyToOne
    @JoinColumn(name = "productVariant_id", nullable = false)
    @JsonIgnore
    private ProductVariant productVariant;
}
