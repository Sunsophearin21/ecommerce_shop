package com.sunsophearin.shopease.entities;

import com.sunsophearin.shopease.security.entities.User;
import jakarta.persistence.*;
import lombok.Data;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Sale details (order items)
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
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

    // Payment method (optional, recommended)
    @Column(name = "payment_method", length = 32)
    private String paymentMethod;

    // Payment status (optional, recommended)
    @Column(name = "payment_status", length = 20)
    private String paymentStatus;

    // Currency (optional, recommended)
    @Column(name = "currency", length = 8)
    private String currency;

    // Delivery address (optional, for shipping)
    @Column(name = "delivery_address", length = 255)
    private String deliveryAddress;

    // Delivery status (optional)
    @Column(name = "delivery_status", length = 20)
    private String deliveryStatus;

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
