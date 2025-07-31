package com.sunsophearin.shopease.controllers;

import com.sunsophearin.shopease.dto.*;
import com.sunsophearin.shopease.dto.response.*;
import com.sunsophearin.shopease.entities.*;
import com.sunsophearin.shopease.mapper.ProductMapper;
import com.sunsophearin.shopease.services.ProductService;

import com.sunsophearin.shopease.services.ProductVariantService;
import com.sunsophearin.shopease.specification.productFilter2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductVariantService productVariantService;
    private final ProductMapper productMapper;

/**
 * Create Product
 */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDto dto){
        return ResponseEntity.ok(productService.createProduct(dto));
    }
    @PostMapping("/product-variantss")
    public ResponseEntity<?> createProductvariant(@ModelAttribute ProductVariantDto ProductVariantdto,@RequestParam MultipartFile[] files) throws IOException{
        ProductVariant productVariant2 = productVariantService.createProductVariant(ProductVariantdto, files);
        return ResponseEntity.ok(productVariant2);
    }

    @PostMapping("/import-stock")
    public ResponseEntity<ImportStock> importStock(@RequestBody ImportStockRequestDTO dto) {
        ImportStock importStock = productService.importProduct(dto);
        return ResponseEntity.ok(importStock);
    }

/**
 * Read Product
 */

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<?> getproductByid(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam(required = false) List<Long> color,
                                        @RequestParam(required = false) List<Long> category,
                                        @RequestParam(required = false) List<Long> size,
                                        @RequestParam(required = false) List<Long> productVariant) {

        productFilter2 filter=new productFilter2();
        filter.setColor(color);
        filter.setCategory(category);
        filter.setSize(size);
        filter.setProductVariant(productVariant);

        return productService.searchProducts(filter);
    }
    @GetMapping("/product-variant/{id}")
    public ResponseEntity<?> getByIdProductVariant(@PathVariable Long id){
        return ResponseEntity.ok(productVariantService.getById(id));
    }
    @GetMapping("/search/{id}")
    public ResponseEntity<?> getProductByVariant(@PathVariable Long id,@RequestParam(required = false) List<Long> productVariant ){
        return ResponseEntity.ok(productService.getProductsByVariant(id,productVariant));
    }
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductVariantByColorAndSize(
            @PathVariable Long productId,
            @RequestParam(value = "color_id", required = false) Long colorId,
            @RequestParam(value = "size_id", required = false) Long sizeId) {

        ProductDtoRespone response = productService.findVariant(productId, colorId, sizeId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{categoryId}/product-detail-page/{productId}")
    public ResponseEntity<?> getProductDetailWithCategory(
            @PathVariable("category") Long categoryId,
            @PathVariable("productId") Long productId,
            @RequestParam(value = "color_id", required = false) Long colorId,
            @RequestParam(value = "size_id", required = false) Long sizeId) {

        // ស្រាច់ពិនិត្យប្រភេទ (category) បើចង់បាន
        // System.out.println("Category: " + category);

        ProductDtoRespone response = productService.findVariant(productId, colorId, sizeId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/by-category-type/{categoryTypeId}")
    public List<ProductDtoRespone> getProductsByCategoryType(@PathVariable Long categoryTypeId) {
        List<Product> products = productService.getProductByCategoryType(categoryTypeId);
        return products.stream()
                .map(productMapper::toDtoList)
                .collect(Collectors.toList());
    }
    @GetMapping("/by-category-item/{categoryItemId}")
    public List<ProductDtoRespone> getProductsByCategoryItem(@PathVariable Long categoryItemId) {
        List<Product> products = productService.getProductByCategoryItem(categoryItemId);
        return products.stream()
                .map(productMapper::toDtoList)
                .collect(Collectors.toList());
    }


    @GetMapping("/get_stock_by_id/{id}")
    public ResponseEntity<?> getStock(@PathVariable Long id){
        return ResponseEntity.ok(productService.getStockById(id));
    }

/**
 * Update Product
 */
    @PostMapping("/product-variant/{id}/update")
    public ResponseEntity<?> upDateProductVariant(@PathVariable Long id,@RequestBody ProductVariantDto dto){
        return ResponseEntity.ok(productVariantService.update(id,dto));
    }

}
