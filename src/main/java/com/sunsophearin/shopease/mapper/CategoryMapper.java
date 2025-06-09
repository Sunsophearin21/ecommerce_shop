package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.CategoryDto;
import com.sunsophearin.shopease.dto.CategoryTypeDto;
import com.sunsophearin.shopease.entities.Category;
import com.sunsophearin.shopease.entities.CategoryType;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

//    @Mapping(target = "categoryTypes", source = "categoryTypeDtos")
    Category categoryDtoToCategory(CategoryDto dto);

//    @Mapping(target = "categoryTypeDtos", source = "categoryTypes")
    CategoryDto categoryToCategoryDto(Category category);
}