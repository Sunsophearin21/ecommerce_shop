package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.SaleDto;
import com.sunsophearin.shopease.dto.response.SaleDtoResponse;
import com.sunsophearin.shopease.entities.Sale;
import com.sunsophearin.shopease.enums.DeliveryStatus;
import com.sunsophearin.shopease.security.entities.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SaleService {
    void enrichSaleDetailDTOs(SaleDto saleDto);
    Sale processSale(SaleDto saleDto, String userEmail);
    BigDecimal calculateTotalPrice(SaleDto saleDto);
    BigDecimal calculateTotalPriceWithDelivery(SaleDto saleDto, User user);
    List<SaleDtoResponse> getSalesByUserNameAndDeliveryStatus(String userName, DeliveryStatus deliveryStatus);
    Map<DeliveryStatus, List<SaleDtoResponse>> getSalesGroupedByStatus(String email);
    List<SaleDtoResponse> getAllSale();

}
