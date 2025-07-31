package com.sunsophearin.shopease.dto;

import lombok.Data;

@Data
public class CategoryItemDto {
    private String name;
    private Long categoryTypeId;
    private String slug;
}
