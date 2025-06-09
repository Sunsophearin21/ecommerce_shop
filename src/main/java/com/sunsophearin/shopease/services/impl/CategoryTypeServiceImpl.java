package com.sunsophearin.shopease.services.impl;

import com.sunsophearin.shopease.dto.CategoryDto;
import com.sunsophearin.shopease.dto.CategoryTypeDto;
import com.sunsophearin.shopease.entities.Category;
import com.sunsophearin.shopease.entities.CategoryType;
import com.sunsophearin.shopease.exception.ResoureApiNotFound;
import com.sunsophearin.shopease.mapper.CategoryMapper;
import com.sunsophearin.shopease.mapper.CategoryTypeMapper;
import com.sunsophearin.shopease.repositories.CategoryRepository;
import com.sunsophearin.shopease.repositories.CategoryTypeRepository;
import com.sunsophearin.shopease.services.CategoryService;
import com.sunsophearin.shopease.services.CategoryTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryTypeServiceImpl implements CategoryTypeService {
    private final CategoryTypeRepository categoryTypeRepository;
    private final CategoryTypeMapper categoryTypeMapper;
    @Override
    public CategoryType createCategoryType(CategoryTypeDto dto) {
        CategoryType entity = categoryTypeMapper.toEntity(dto);
        CategoryType save = categoryTypeRepository.save(entity);
        return save;
    }

    @Override
    public CategoryType getCategoryTypeById(Long id) {
        return categoryTypeRepository.findById(id).orElseThrow(()->new ResoureApiNotFound("CategoryType",id));
    }
}
