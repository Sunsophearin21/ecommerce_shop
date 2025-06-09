package com.sunsophearin.shopease.controllers;

import com.sunsophearin.shopease.dto.CategoryDto;
import com.sunsophearin.shopease.entities.Category;
import com.sunsophearin.shopease.repositories.CategoryRepository;
import com.sunsophearin.shopease.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto dto){
        Category category = categoryService.createCategory(dto);
        return ResponseEntity.ok(category);
    }
    @GetMapping
    public ResponseEntity<?> getAllCategories(){
        List<Category> allCategory = categoryService.getAllCategory();
        return ResponseEntity.ok(allCategory);
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id){
        Category categoryById = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryById);
    }
}
