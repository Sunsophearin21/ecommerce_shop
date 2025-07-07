package com.sunsophearin.shopease.dto;

import com.sunsophearin.shopease.entities.ProductVariant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ProductDtoRespone {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer discount;       // ✅ បញ្ចុះតម្លៃ (%)
    private BigDecimal finalPrice;  // ✅ តម្លៃក្រោយបញ្ចុះ
    private BigDecimal deliveryFee;


    private Date createAt;
    private Date updateAt;

    private CategoryDto category;
    private CategoryTypeDto categoryType;
    private List<ProductVariant> productVariants;
    private MenuFactureDto menuFacturer;
    private boolean newArrival;
}
