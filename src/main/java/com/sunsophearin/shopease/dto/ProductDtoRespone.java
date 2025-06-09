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
    private boolean newArrival;
    private Date createAt;
    private Date updateAt;

    private CategoryDto category;
    private CategoryTypeDto categoryType;
    private MenuFactureDto menuFacturer;
    private ProductVariantDto productVariant;
    private List<ProductVariantDto> productVariants;
}
