package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.DeliveryFeeDto;
import com.sunsophearin.shopease.entities.DeliveryFee;
import com.sunsophearin.shopease.mapper.DeliveryFeMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DeliveryFeeService {
    List<DeliveryFee> getAllDeliveryFees();
    DeliveryFee getDeliveryFeeById(Long id);
    DeliveryFee createDeliveryFee(DeliveryFeeDto dto, MultipartFile imageFile) throws IOException;
    DeliveryFee updateDeliveryFee(Long id, DeliveryFeMapper dto);
    void deleteDeliveryFee(Long id);
}
