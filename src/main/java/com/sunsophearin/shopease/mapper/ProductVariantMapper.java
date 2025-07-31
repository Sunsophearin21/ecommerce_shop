package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.ProductVariantDto;
import com.sunsophearin.shopease.entities.ProductVariant;
import com.sunsophearin.shopease.services.ColorService;
import com.sunsophearin.shopease.services.SizeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ColorService.class, SizeService.class,StockMapper.class,})
public interface ProductVariantMapper {

    @Mapping(target = "color", source = "colorId")
    @Mapping(target = "product", ignore = true) // Remove product mapping
    ProductVariant productVariantDtoToProductVariant(ProductVariantDto dto);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "colorId", source = "color.id")
    ProductVariantDto productVariantToProductVariantDto(ProductVariant entity);
}
