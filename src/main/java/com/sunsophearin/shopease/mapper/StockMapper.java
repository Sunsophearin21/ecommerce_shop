package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.StockDto;
import com.sunsophearin.shopease.entities.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SizeMapper.class})
public interface StockMapper {
    StockDto toDto(Stock entity);
    List<StockDto> toDtoList(List<Stock> stocks);
}
