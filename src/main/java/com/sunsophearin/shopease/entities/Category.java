package com.sunsophearin.shopease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private String description;
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
//    @JsonIgnore
    @ToString.Exclude
    private List<CategoryType> categoryTypes;
}
