package com.sunsophearin.shopease.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductVariantDto {
    private Long productId;
    private Long colorId;
    private List<String> imageUrls;
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    private List<StockDto> stockDtos;

}
