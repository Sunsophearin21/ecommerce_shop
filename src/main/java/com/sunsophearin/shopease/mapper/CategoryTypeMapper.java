package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.CategoryTypeDto;
import com.sunsophearin.shopease.entities.CategoryType;
import com.sunsophearin.shopease.services.CategoryService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {CategoryService.class})
public interface CategoryTypeMapper {
    @Mapping(target = "category",source = "categoryId")
    CategoryType toEntity(CategoryTypeDto dto);
}
