package com.sunsophearin.shopease.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String slug;
    private List<CategoryTypeDto> categoryTypes;
}
