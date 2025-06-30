package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.PaymentRequest;
import com.sunsophearin.shopease.dto.SaleDto;
import com.sunsophearin.shopease.dto.SaleRequestDTO;

import java.util.Map;

public interface PaymentService {
    Map<String, Object> generateKhqr(SaleDto request, String userEmail);
    Map<String, Object> generateKhqr2(PaymentRequest request, String userEmail);
    Map<String, Object> checkPayment(String md5);
    void confirmAndSaveSale(PaymentRequest dto, String userEmail, String md5);
}
