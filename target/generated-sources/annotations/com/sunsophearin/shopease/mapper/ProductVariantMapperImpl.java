package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.ProductVariantDto;
import com.sunsophearin.shopease.dto.StockDto;
import com.sunsophearin.shopease.entities.Color;
import com.sunsophearin.shopease.entities.Product;
import com.sunsophearin.shopease.entities.ProductVariant;
import com.sunsophearin.shopease.entities.Stock;
import com.sunsophearin.shopease.services.ColorService;
import com.sunsophearin.shopease.services.SizeService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-02T15:38:59+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ProductVariantMapperImpl implements ProductVariantMapper {

    @Autowired
    private ColorService colorService;
    @Autowired
    private SizeService sizeService;
    @Autowired
    private StockMapper stockMapper;

    @Override
    public ProductVariant productVariantDtoToProductVariant(ProductVariantDto dto) {
        if ( dto == null ) {
            return null;
        }

        ProductVariant productVariant = new ProductVariant();

        productVariant.setColor( colorService.getColorById( dto.getColorId() ) );
        List<String> list = dto.getImages();
        if ( list != null ) {
            productVariant.setImages( new ArrayList<String>( list ) );
        }
        productVariant.setStocks( stockDtoListToStockList( dto.getStocks() ) );

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
        List<String> list = entity.getImages();
        if ( list != null ) {
            productVariantDto.setImages( new ArrayList<String>( list ) );
        }
        productVariantDto.setStocks( stockMapper.toDtoList( entity.getStocks() ) );

        return productVariantDto;
    }

    protected Stock stockDtoToStock(StockDto stockDto) {
        if ( stockDto == null ) {
            return null;
        }

        Stock.StockBuilder stock = Stock.builder();

        stock.id( stockDto.getId() );
        stock.currentQuantity( stockDto.getCurrentQuantity() );
        if ( stockDto.getAverageImportPrice() != null ) {
            stock.averageImportPrice( stockDto.getAverageImportPrice().doubleValue() );
        }
        stock.size( sizeService.createSizw( stockDto.getSize() ) );

        return stock.build();
    }

    protected List<Stock> stockDtoListToStockList(List<StockDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Stock> list1 = new ArrayList<Stock>( list.size() );
        for ( StockDto stockDto : list ) {
            list1.add( stockDtoToStock( stockDto ) );
        }

        return list1;
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
