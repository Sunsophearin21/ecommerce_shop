package com.sunsophearin.shopease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "colors")
public class Color {
    @Id
    @Column(name = "color_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "color_name")
    private String name;
    @OneToMany(mappedBy = "color",cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<ProductVariant> productVariants;
}
