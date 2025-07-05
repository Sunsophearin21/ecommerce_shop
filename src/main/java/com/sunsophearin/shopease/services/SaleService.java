package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.SaleDto;
import com.sunsophearin.shopease.dto.SaleRequestDTO;
import com.sunsophearin.shopease.entities.Product;
import com.sunsophearin.shopease.entities.Sale;
import com.sunsophearin.shopease.security.entities.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.math.BigDecimal;
import java.util.Map;

public interface SaleService {
    void enrichSaleDetailDTOs(SaleDto saleDto);
    Sale processSale(SaleDto saleDto, String userEmail);
    BigDecimal calculateTotalPrice(SaleDto saleDto);

}
