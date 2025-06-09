package com.sunsophearin.shopease.specification;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class productFilter2 {
    private List<Long> id;
    private List<Long> category;
    private List<Long> categoryType;
    private List<Long> menuFacturer;
    private List<Long> size;
    private List<Long> color;
    private List<Long> productVariant;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
