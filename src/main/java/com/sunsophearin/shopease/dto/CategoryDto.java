package com.sunsophearin.shopease.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
    private String name;
    private String code;
    private String description;
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    private List<CategoryTypeDto> categoryTypeDtos;
}
