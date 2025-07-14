package com.sunsophearin.shopease.mapper;
import com.sunsophearin.shopease.dto.SaleDto;
import com.sunsophearin.shopease.dto.response.SaleDetailDtoResponse;
import com.sunsophearin.shopease.dto.response.SaleDtoResponse;
import com.sunsophearin.shopease.entities.Sale;
import com.sunsophearin.shopease.entities.SaleDetail;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleMapper {

//    @Mapping(source = "saleDetailDTOS", target = "saleDetails")
//    SaleDto saleToSaleDto(Sale sale);

    List<SaleDtoResponse> salesToSaleDtos(List<Sale> sales);

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "productVariant.id", target = "productVariantName")
    @Mapping(source = "size.name", target = "sizeName")
    @Mapping(source = "color.name", target = "colorName")
    @Mapping(source = "productVariant.images", target = "images")
    SaleDetailDtoResponse saleDetailToSaleDetailDto(SaleDetail saleDetail);

    List<SaleDetailDtoResponse> saleDetailsToSaleDetailDtos(List<SaleDetail> saleDetails);
}