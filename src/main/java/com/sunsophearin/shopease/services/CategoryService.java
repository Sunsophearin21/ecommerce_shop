package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.CategoryDto;
import com.sunsophearin.shopease.entities.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryDto dto);
    List<Category> getAllCategory();
    Category getCategoryById(Long id);
}
