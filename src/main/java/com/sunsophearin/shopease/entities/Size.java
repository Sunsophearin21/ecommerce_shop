package com.sunsophearin.shopease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "sizes")
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "size_id")
    private Long id;
    @Column(name = "size_name")
    private String name;
    @OneToMany(mappedBy = "size", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Stock> stocks;
}
