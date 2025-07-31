package com.sunsophearin.shopease.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryTypeDto {
    private Long categoryId;
    private String name;
    private String code;
    private String description;
    private String slug; // ✅ Add this line
    private List<CategoryItemDto> categoryItems;
}
