package com.sunsophearin.shopease.entities;

import com.sunsophearin.shopease.security.entities.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "sale",cascade = CascadeType.ALL)
    private List<SaleDetail> saleDetails;
    private int quantity;
    private BigDecimal finalPrice;
    private String transactionId;
    private String khqrMd5;
    private String status; // e.g. PENDING, PAID, FAILED

    @Column(name = "sale_createAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @Column(name = "sale_upDateAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

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
