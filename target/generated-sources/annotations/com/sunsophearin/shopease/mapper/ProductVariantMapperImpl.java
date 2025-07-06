package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.ProductVariantDto;
import com.sunsophearin.shopease.entities.Color;
import com.sunsophearin.shopease.entities.Product;
import com.sunsophearin.shopease.entities.ProductVariant;
import com.sunsophearin.shopease.services.ColorService;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-06T14:25:29+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ProductVariantMapperImpl implements ProductVariantMapper {

    @Autowired
    private ColorService colorService;

    @Override
    public ProductVariant productVariantDtoToProductVariant(ProductVariantDto dto) {
        if ( dto == null ) {
            return null;
        }

        ProductVariant productVariant = new ProductVariant();

        productVariant.setColor( colorService.getColorById( dto.getColorId() ) );

        return productVariant;
    }

    @Override
    public ProductVariantDto productVariantToProductVariantDto(ProductVariant entity) {
        if ( entity == null ) {
            return null;
        }

        ProductVariantDto productVariantDto = new ProductVariantDto();

        productVariantDto.setProductId( entityProductId( entity ) );
        productVariantDto.setColorId( entityColorId( entity ) );

        return productVariantDto;
    }

    private Long entityProductId(ProductVariant productVariant) {
        Product product = productVariant.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private Long entityColorId(ProductVariant productVariant) {
        Color color = productVariant.getColor();
        if ( color == null ) {
            return null;
        }
        return color.getId();
    }
}
