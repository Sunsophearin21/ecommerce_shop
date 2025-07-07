package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.SaleDto;
import com.sunsophearin.shopease.entities.Sale;

import java.math.BigDecimal;
public interface SaleService {
    void enrichSaleDetailDTOs(SaleDto saleDto);
    Sale processSale(SaleDto saleDto, String userEmail);
    BigDecimal calculateTotalPrice(SaleDto saleDto);
    BigDecimal calculateTotalPriceWithDelivery(SaleDto saleDto);

}
