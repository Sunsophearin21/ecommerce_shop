package com.sunsophearin.shopease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "import_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "import_price_per_unit", nullable = false)
    private double importPricePerUnit;

    @Column(name = "import_date")
    private LocalDateTime importDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    @JsonIgnore
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "size_id")
    @JsonIgnore
    private Size size;
}

