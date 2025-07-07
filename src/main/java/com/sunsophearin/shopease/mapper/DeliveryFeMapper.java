package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.DeliveryFeeDto;
import com.sunsophearin.shopease.entities.DeliveryFee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryFeMapper {
    DeliveryFee toEntity(DeliveryFeeDto dto);
    DeliveryFeeDto toDto (DeliveryFee deliveryFee);
}
