package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.CategoryDto;
import com.sunsophearin.shopease.dto.CategoryTypeDto;
import com.sunsophearin.shopease.entities.Category;
import com.sunsophearin.shopease.entities.CategoryType;

import java.util.List;

public interface CategoryTypeService {
    CategoryType createCategoryType(CategoryTypeDto dto);
//    List<Category> getAllCategory();
    CategoryType getCategoryTypeById(Long id);
}
