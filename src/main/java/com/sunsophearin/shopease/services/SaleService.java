package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.SaleDto;
import com.sunsophearin.shopease.dto.response.SaleDtoResponse;
import com.sunsophearin.shopease.entities.Sale;
import com.sunsophearin.shopease.enums.DeliveryStatus;

import java.math.BigDecimal;
import java.util.List;

public interface SaleService {
    void enrichSaleDetailDTOs(SaleDto saleDto);
    Sale processSale(SaleDto saleDto, String userEmail);
    BigDecimal calculateTotalPrice(SaleDto saleDto);
    BigDecimal calculateTotalPriceWithDelivery(SaleDto saleDto);
    List<SaleDtoResponse> getSalesByUserNameAndDeliveryStatus(String userName, DeliveryStatus deliveryStatus);
    List<SaleDtoResponse> getAllSale();

}
