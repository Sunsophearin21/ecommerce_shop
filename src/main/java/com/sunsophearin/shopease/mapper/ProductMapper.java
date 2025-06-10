package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.*;
import com.sunsophearin.shopease.entities.*;
import com.sunsophearin.shopease.services.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        CategoryService.class,
        CategoryTypeService.class,
        MenuFacturerService.class,
        ProductVariantService.class
})
public interface ProductMapper {

    // Create (DTO to entity)
    @Mapping(target = "categoryType", source = "categoryTypeId")
    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "menuFacturer", source = "menuFacturerId")
    Product productDtoToProduct(ProductDto dto);

    // Read (Entity to DTO)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "categoryType", source = "categoryType")
    @Mapping(target = "menuFacturer", source = "menuFacturer")
    @Mapping(target = "productVariants", source = "productVariants")

    // ✅ Map discount directly
    @Mapping(target = "discount", source = "discount")

    // ✅ Map finalPrice using method (manually mapped via expression)
    @Mapping(target = "finalPrice", expression = "java(product.getFinalPrice())")
    ProductDtoRespone toDtoList(Product product);
}
