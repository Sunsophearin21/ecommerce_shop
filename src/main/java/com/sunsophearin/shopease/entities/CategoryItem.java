package com.sunsophearin.shopease.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CategoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryItem_id")
    private Long id;

    private String name; // e.g. T-Shirts, Heels, Skirts

    @ManyToOne
    @JoinColumn(name = "category_type_id")
    private CategoryType categoryType;
}
