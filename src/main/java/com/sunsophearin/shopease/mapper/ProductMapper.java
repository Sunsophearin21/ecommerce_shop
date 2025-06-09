package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.ProductDto;
import com.sunsophearin.shopease.dto.ProductDtoRespone;
import com.sunsophearin.shopease.entities.*;
import com.sunsophearin.shopease.services.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {CategoryService.class,
        CategoryTypeService.class, MenuFacturerService.class,ProductVariantMapper.class })
public interface ProductMapper {
    @Mapping(target = "categoryType",source = "categoryTypeId")
    @Mapping(target = "category",source = "categoryId")
    @Mapping(target = "menuFacturer",source = "menuFacturerId")
    Product productDtoToProduct(ProductDto dto);
//    @Mapping(target = "categoryId",source = "category.id")
//    @Mapping(target = "categoryTypeId",source = "categoryType.id")
//    @Mapping(target = "menuFacturerId",source = "menuFacturer.id")
////    @Mapping(target = "productVariant",source = "productVariants.id")
//    ProductDto productToProductDto(Product product);

//    ProductDtoRespone productToProductDtoRespone(Product product);
}
