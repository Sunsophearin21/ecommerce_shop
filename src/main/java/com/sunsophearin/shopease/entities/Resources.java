package com.sunsophearin.shopease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_resources")
@Data
public class Resources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resources_id")
    private Long id;
    private List<String> images;
    private String type;
//    @OneToMany(mappedBy = "resources",cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<ProductVariant> productVariants;

}
