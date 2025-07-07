package com.sunsophearin.shopease.services.impl;

import com.sunsophearin.shopease.dto.DeliveryFeeDto;
import com.sunsophearin.shopease.entities.DeliveryFee;
import com.sunsophearin.shopease.exception.ResoureApiNotFound;
import com.sunsophearin.shopease.mapper.DeliveryFeMapper;
import com.sunsophearin.shopease.repositories.DeliveryFeeRepository;
import com.sunsophearin.shopease.services.DeliveryFeeService;
import com.sunsophearin.shopease.services.UploadImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DeliveryFeeServiceImpl implements DeliveryFeeService {
    private final DeliveryFeeRepository deliveryFeeRepository;
    private final DeliveryFeMapper deliveryFeMapper;
    private  final UploadImageFileService uploadImageFileService;

    @Override
    public List<DeliveryFee> getAllDeliveryFees() {
        return List.of();
    }

    @Override
    public DeliveryFee getDeliveryFeeById(Long id) {
        return deliveryFeeRepository.findById(id).orElseThrow(()->new ResoureApiNotFound("Delivery Fee ",id));
    }

    @Override
    public DeliveryFee createDeliveryFee(DeliveryFeeDto dto, MultipartFile imageFile) throws IOException {
        DeliveryFee entity = deliveryFeMapper.toEntity(dto);
        String img = uploadImageFileService.uploadImage(imageFile);
        entity.setImage(img);
        return deliveryFeeRepository.save(entity);
    }

    @Override
    public DeliveryFee updateDeliveryFee(Long id, DeliveryFeMapper dto) {
        return null;
    }

    @Override
    public void deleteDeliveryFee(Long id) {

    }
}
