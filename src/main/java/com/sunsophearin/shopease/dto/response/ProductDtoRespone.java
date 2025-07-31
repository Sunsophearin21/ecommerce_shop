package com.sunsophearin.shopease.dto.response;

import com.sunsophearin.shopease.dto.CategoryDto;
import com.sunsophearin.shopease.dto.CategoryItemDto;
import com.sunsophearin.shopease.dto.CategoryTypeDto;
import com.sunsophearin.shopease.dto.MenuFactureDto;
import com.sunsophearin.shopease.entities.ProductVariant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ProductDtoRespone {
    private Long id;
    private String slug;
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
    private CategoryItemDto categoryItem;
    private List<ProductVariant> productVariants;
    private MenuFactureDto menuFacturer;
    private boolean newArrival;
}
