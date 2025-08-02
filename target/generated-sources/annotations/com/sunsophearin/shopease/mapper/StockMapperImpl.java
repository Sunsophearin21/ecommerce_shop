package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.SizeDto;
import com.sunsophearin.shopease.dto.StockDto;
import com.sunsophearin.shopease.entities.Size;
import com.sunsophearin.shopease.entities.Stock;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-02T14:10:43+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class StockMapperImpl implements StockMapper {

    @Override
    public StockDto toDto(Stock entity) {
        if ( entity == null ) {
            return null;
        }

        StockDto stockDto = new StockDto();

        stockDto.setId( entity.getId() );
        stockDto.setCurrentQuantity( entity.getCurrentQuantity() );
        if ( entity.getAverageImportPrice() != null ) {
            stockDto.setAverageImportPrice( BigDecimal.valueOf( entity.getAverageImportPrice() ) );
        }
        stockDto.setSize( sizeToSizeDto( entity.getSize() ) );

        return stockDto;
    }

    @Override
    public List<StockDto> toDtoList(List<Stock> stocks) {
        if ( stocks == null ) {
            return null;
        }

        List<StockDto> list = new ArrayList<StockDto>( stocks.size() );
        for ( Stock stock : stocks ) {
            list.add( toDto( stock ) );
        }

        return list;
    }

    protected SizeDto sizeToSizeDto(Size size) {
        if ( size == null ) {
            return null;
        }

        SizeDto sizeDto = new SizeDto();

        sizeDto.setName( size.getName() );

        return sizeDto;
    }
}
