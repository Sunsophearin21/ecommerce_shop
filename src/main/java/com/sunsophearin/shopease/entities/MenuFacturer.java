package com.sunsophearin.shopease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity
@Table(name = "menu_facturer")
public class MenuFacturer {
    @Id
    @Column(name = "menufac_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "menufac_name")
    private String name;
    @OneToMany(mappedBy = "menuFacturer",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;
}
