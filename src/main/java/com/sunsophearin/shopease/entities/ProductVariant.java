package com.sunsophearin.shopease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "product_variant")
@Data
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;
    private List<String> images;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;
    // Stock related to this variant (different sizes)
    @OneToMany(mappedBy = "productVariant", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Stock> stocks;
    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ImportStock> importStocks;
}

