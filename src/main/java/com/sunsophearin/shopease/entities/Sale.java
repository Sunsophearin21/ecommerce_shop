package com.sunsophearin.shopease.entities;

import com.sunsophearin.shopease.enums.DeliveryStatus;
import com.sunsophearin.shopease.security.entities.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    // Reference to the customer/user who placed the order
    @ManyToOne
    @JoinColumn(name = "user_id")
//    @JsonIgnore
    private User user;

    // Sale details (order items)
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    @ToString.Exclude
//    @JsonIgnore
    private List<SaleDetail> saleDetails;

    // Total quantity of all items in the sale
    private int quantity;

    // Final total price of the sale (after discount, etc.)
    @Column(name = "final_price", precision = 12, scale = 2)
    private BigDecimal finalPrice;

    // Unique transaction/order ID for external reference
    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(name = "order_id", unique = true, nullable = false, length = 40)
    private String orderId;

    // For KHQR payment tracking (if used)
    @Column(name = "khqr_md5")
    private String khqrMd5;

    // Status of the sale (e.g. PENDING, PAID, FAILED, CANCELLED)
    @Column(name = "status", length = 20)
    private String status;

    // Delivery status (optional)
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", length = 20)
    private DeliveryStatus deliveryStatus;

    // Notes (optional, for admin/customer remarks)
    @Column(name = "notes", length = 255)
    private String notes;

    // Timestamps
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sale_createAt", updatable = false)
    private Date createAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sale_updateAt")
    private Date updateAt;
    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;


    @PrePersist
    protected void onCreate() {
        createAt = new Date();
        updateAt = createAt;
        if (this.orderId == null || this.orderId.isEmpty()) {
            String datePart = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
            String randomPart = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            this.orderId = "ORD-" + datePart + "-" + randomPart;
        }
        createAt = new Date();
        updateAt = createAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updateAt = new Date();
    }

}
