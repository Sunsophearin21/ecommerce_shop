package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.DeliveryFeeDto;
import com.sunsophearin.shopease.entities.DeliveryFee;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-31T15:46:55+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class DeliveryFeMapperImpl implements DeliveryFeMapper {

    @Override
    public DeliveryFee toEntity(DeliveryFeeDto dto) {
        if ( dto == null ) {
            return null;
        }

        DeliveryFee deliveryFee = new DeliveryFee();

        deliveryFee.setImage( dto.getImage() );
        deliveryFee.setName( dto.getName() );
        deliveryFee.setPrice( dto.getPrice() );
        deliveryFee.setDescription( dto.getDescription() );
        deliveryFee.setCompany( dto.getCompany() );
        deliveryFee.setDelivery( dto.getDelivery() );

        return deliveryFee;
    }

    @Override
    public DeliveryFeeDto toDto(DeliveryFee deliveryFee) {
        if ( deliveryFee == null ) {
            return null;
        }

        DeliveryFeeDto deliveryFeeDto = new DeliveryFeeDto();

        deliveryFeeDto.setImage( deliveryFee.getImage() );
        deliveryFeeDto.setName( deliveryFee.getName() );
        deliveryFeeDto.setPrice( deliveryFee.getPrice() );
        deliveryFeeDto.setDescription( deliveryFee.getDescription() );
        deliveryFeeDto.setCompany( deliveryFee.getCompany() );
        deliveryFeeDto.setDelivery( deliveryFee.getDelivery() );

        return deliveryFeeDto;
    }
}
