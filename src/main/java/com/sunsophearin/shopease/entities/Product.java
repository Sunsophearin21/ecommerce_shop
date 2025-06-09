package com.sunsophearin.shopease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "proucts")
@Data
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_name")
    private String name;
    @Column(name = "product_des")
    private String description;
    @Column(name = "product_price")
    private BigDecimal price;
    @Column(name = "product_isNewArr")
    private  boolean isNewArrival;
    @Column(name = "product_createAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @Column(name = "product_upDateAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "categoryType_id",nullable = false)
    private CategoryType categoryType;
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<ProductVariant> productVariants;
    @ManyToOne
    @JoinColumn(name = "menufac_id",nullable = false)
    private MenuFacturer menuFacturer;
    @PrePersist
    protected void onCreate(){
        createAt = new Date();
        updateAt =createAt;
    }
    @PreUpdate
    protected void onUpdate(){
        updateAt= new Date();
    }
}
