package com.sunsophearin.shopease.controllers;

import com.sunsophearin.shopease.dto.CategoryDto;
import com.sunsophearin.shopease.dto.CategoryTypeDto;
import com.sunsophearin.shopease.entities.Category;
import com.sunsophearin.shopease.entities.CategoryType;
import com.sunsophearin.shopease.mapper.CategoryTypeMapper;
import com.sunsophearin.shopease.services.CategoryService;
import com.sunsophearin.shopease.services.CategoryTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categoryType")
@RequiredArgsConstructor
public class CategoryTypeController {
    private final CategoryTypeService categoryTypeService;
    private final CategoryTypeMapper categoryTypeMapper;
    @PostMapping
    public ResponseEntity<?> createCategoryType(@RequestBody CategoryTypeDto dto){
        CategoryType categoryType = categoryTypeService.createCategoryType(dto);
        return ResponseEntity.ok(categoryType);
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getCategoryTypeByid(@PathVariable Long id){
        return ResponseEntity.ok(categoryTypeMapper.toDto(categoryTypeService.getCategoryTypeById(id)));
    }
}
