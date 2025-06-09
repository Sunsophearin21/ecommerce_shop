package com.sunsophearin.shopease.specification;

import lombok.Data;

@Data
public class ProductFilter {
    private Long id;
    private Long category;
    private Long categoryType;
    private Long menuFacturer;
    private Long color;
    private Long size;
    private Long productVariant;

    String PRODUCT_ID="id";
    String PRODUCT_CATEGORYID="category";
    String PRODUCT_CATEGORYTYPEID="categoryType";
    String PRODUCT_MENUFACTURERID="menuFacturer";
    String PRODUCT_COLORID="color";
    String PRODUCT_SIZEID="size";
    String PRODUCT_VARIANT="productVariant";

}
