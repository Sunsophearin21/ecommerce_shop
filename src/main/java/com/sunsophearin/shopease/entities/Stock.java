package com.sunsophearin.shopease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"product_variant_id", "size_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "current_quantity", nullable = false)
    private int currentQuantity;

    @Column(name = "average_import_price")
    private Double averageImportPrice;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "product_variant_id",nullable = false)
    @JsonIgnore
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "size_id",nullable = false)
    private Size size;

    @PreUpdate
    @PrePersist
    public void updateTimestamp() {
        lastUpdated = LocalDateTime.now();
    }
}
