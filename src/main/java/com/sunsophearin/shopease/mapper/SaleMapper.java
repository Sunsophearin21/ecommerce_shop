package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.response.SaleDetailDtoResponse;
import com.sunsophearin.shopease.dto.response.SaleDtoResponse;
import com.sunsophearin.shopease.entities.Sale;
import com.sunsophearin.shopease.entities.SaleDetail;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    // ✅ Add this line
    SaleDtoResponse saleToSaleDto(Sale sale);

    List<SaleDtoResponse> salesToSaleDtos(List<Sale> sales);

    List<SaleDetailDtoResponse> saleDetailsToSaleDetailDtos(List<SaleDetail> saleDetails);

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "productVariant.id", target = "productVariantName") // consider changing to .name if needed
    @Mapping(source = "size.name", target = "sizeName")
    @Mapping(source = "color.name", target = "colorName")
    @Mapping(source = "productVariant.images", target = "images")
    SaleDetailDtoResponse saleDetailToSaleDetailDto(SaleDetail saleDetail);
}
