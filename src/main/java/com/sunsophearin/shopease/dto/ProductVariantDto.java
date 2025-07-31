package com.sunsophearin.shopease.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductVariantDto {
    private Long productId;
    private Long colorId;
    private List<String> images;
    private List<StockDto> stocks;
//    private List<ImportStockRequestDTO> importStocks;
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    private List<StockDto> stockDtos;

}
