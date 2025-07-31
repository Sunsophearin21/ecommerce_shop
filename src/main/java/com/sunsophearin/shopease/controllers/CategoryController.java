package com.sunsophearin.shopease.controllers;

import com.sunsophearin.shopease.dto.CategoryDto;
import com.sunsophearin.shopease.entities.Category;
import com.sunsophearin.shopease.mapper.CategoryMapper;
import com.sunsophearin.shopease.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    // ✅ Create new category
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto dto) {
        Category category = categoryService.createCategory(dto);
        CategoryDto response = categoryMapper.categoryToCategoryDto(category);
        return ResponseEntity.ok(response);
    }

    // ✅ Get all categories as DTO (with slugs and nested categoryTypes)
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategory();
        List<CategoryDto> dtos = categoryMapper.toDtoList(categories);
        return ResponseEntity.ok(dtos);
    }

    // ✅ Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        CategoryDto dto = categoryMapper.categoryToCategoryDto(category);
        return ResponseEntity.ok(dto);
    }

    // ✅ (Optional/Advanced): Get category by slug
//    @GetMapping("/slug/{slug}")
//    public ResponseEntity<CategoryDto> getCategoryBySlug(@PathVariable String slug) {
//        Category category = categoryService.getCategoryBySlug(slug); // you implement this in service
//        CategoryDto dto = categoryMapper.categoryToCategoryDto(category);
//        return ResponseEntity.ok(dto);
//    }
}
