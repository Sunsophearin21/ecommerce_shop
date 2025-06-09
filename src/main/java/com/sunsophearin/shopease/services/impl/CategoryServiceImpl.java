package com.sunsophearin.shopease.services.impl;

import com.sunsophearin.shopease.dto.CategoryDto;
import com.sunsophearin.shopease.entities.Category;
import com.sunsophearin.shopease.exception.ResoureApiNotFound;
import com.sunsophearin.shopease.mapper.CategoryMapper;
import com.sunsophearin.shopease.repositories.CategoryRepository;
import com.sunsophearin.shopease.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    @Override
    public Category createCategory(CategoryDto dto) {
        Category category = categoryMapper.categoryDtoToCategory(dto);
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()-> new ResoureApiNotFound("Category",id));
    }
}
