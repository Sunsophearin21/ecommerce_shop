package com.sunsophearin.shopease.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunsophearin.shopease.dto.*;
import com.sunsophearin.shopease.entities.Product;
import com.sunsophearin.shopease.entities.ProductVariant;
import com.sunsophearin.shopease.entities.Resources;
import com.sunsophearin.shopease.mapper.ProductMapper;
import com.sunsophearin.shopease.repositories.ResourcesRepository;
import com.sunsophearin.shopease.services.ProductService;

import com.sunsophearin.shopease.services.ProductVariantService;
import com.sunsophearin.shopease.services.ResourcesService;
import com.sunsophearin.shopease.specification.productFilter2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ResourcesService resources;
    private final ProductVariantService productVariantService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDto dto){
        return ResponseEntity.ok(productService.createProduct(dto));
    }
//    @GetMapping("{id}")
//    public ResponseEntity<?> getByid(@PathVariable Long id){
//        return ResponseEntity.ok(productService.getProductById(id));
//    }
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
    @PostMapping("/add_resources")
    public ResponseEntity<?> addResources(
            @RequestParam String resourcesDTO,
            @RequestParam MultipartFile[] files) throws IOException {

        ResourcesDto dto = convert(resourcesDTO);
        Resources created = resources.create(dto, files);
        return ResponseEntity.ok(created);
    }
    @GetMapping("/product-variant/{id}")
    public ResponseEntity<?> getByIdProductVariant(@PathVariable Long id){
        return ResponseEntity.ok(productVariantService.getById(id));
    }
    @PostMapping("/product-variant/{id}/update")
    public ResponseEntity<?> upDateProductVariant(@PathVariable Long id,@RequestBody ProductVariantDto dto){
        return ResponseEntity.ok(productVariantService.update(id,dto));
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> getProductByVariant(@PathVariable Long id,@RequestParam(required = false) List<Long> productVariant ){
        return ResponseEntity.ok(productService.getProductsByVariant(id,productVariant));
    }
    @PostMapping("/product-variantss")
    public ResponseEntity<?> createProductvariant(@RequestParam String ProductVariantdto,@RequestParam MultipartFile[] files) throws IOException{
        ProductVariantDto dto = convertToProductVariant(ProductVariantdto);
        ProductVariant productVariant2 = productVariantService.createProductVariant(dto, files);
        return ResponseEntity.ok(productVariant2);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductVariantByColorAndSize(
            @PathVariable Long id,
            @RequestParam(required = false) List<Long> colorIds) {

        Product variant = productService.findVariant(id, colorIds);
        return ResponseEntity.ok(variant);
    }





    private ProductDto convertToProductDto(String peoductDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(peoductDtoObj, ProductDto.class);
    }
    private ResourcesDto convert(String resources) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(resources, ResourcesDto.class);
    }
    private ProductVariantDto convertToProductVariant(String peoductVariantDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(peoductVariantDtoObj, ProductVariantDto.class);
    }
}
