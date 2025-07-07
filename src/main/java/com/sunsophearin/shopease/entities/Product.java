package com.sunsophearin.shopease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

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
    @ToString.Exclude
    private List<ProductVariant> productVariants;
    @ManyToOne
    @JoinColumn(name = "menufac_id",nullable = false)
    private MenuFacturer menuFacturer;
    @Column(name = "product_discount")
    private Integer discount = 0; // បញ្ចុះតម្លៃជាភាគរយ (%), default = 0
    @Column(name = "delivery_fee", precision = 10, scale = 2, nullable = true)
    private BigDecimal deliveryFee;

    @Transient //@Transient បញ្ជាក់ថា finalPrice មិនត្រូវរក្សាទុកក្នុង database, គឺគ្រាន់តែគណនាឡើងសម្រាប់ API/json response ប៉ុណ្ណោះ
    public BigDecimal getFinalPrice() {
        if (discount != null && discount > 0) {
            return price.subtract(price.multiply(BigDecimal.valueOf(discount)).divide(BigDecimal.valueOf(100)));
        }
        return price;
    }
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
