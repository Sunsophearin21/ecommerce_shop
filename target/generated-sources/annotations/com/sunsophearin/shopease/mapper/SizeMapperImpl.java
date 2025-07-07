package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.SizeDto;
import com.sunsophearin.shopease.entities.Size;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-07T16:39:57+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class SizeMapperImpl implements SizeMapper {

    @Override
    public Size SizeDtoToSize(SizeDto dto) {
        if ( dto == null ) {
            return null;
        }

        Size size = new Size();

        size.setName( dto.getName() );

        return size;
    }
}
