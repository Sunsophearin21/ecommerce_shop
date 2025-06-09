package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.ProductVariantDto;
import com.sunsophearin.shopease.entities.ProductVariant;
import com.sunsophearin.shopease.services.ColorService;
import com.sunsophearin.shopease.services.ProductService;
import com.sunsophearin.shopease.services.ResourcesService;
import com.sunsophearin.shopease.services.SizeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {ColorService.class, ProductService.class, SizeService.class})
public interface ProductVariantMapper {
//    @Mapping(target = "color",source = "colorId")
//    @Mapping(target = "product",source = "productId")
//    @Mapping(target = "size",source = "sizeId")
//    @Mapping(target = "resources",source = "resourcesId")
//    ProductVariant productVariantDtoToProductVariant(ProductVariantDto dto);

    @Mapping(target = "color",source = "colorId")
    @Mapping(target = "product",source = "productId")
    ProductVariant productVariantDtoToProductVariant(ProductVariantDto dto);
    @Mapping(target = "productId",source = "product.id")
    @Mapping(target = "colorId",source = "color.id")
    ProductVariantDto productVariantToProductVariantDto(ProductVariant productVariant);
}
