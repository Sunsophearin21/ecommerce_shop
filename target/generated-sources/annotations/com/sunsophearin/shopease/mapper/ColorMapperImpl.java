package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.ColorDto;
import com.sunsophearin.shopease.entities.Color;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-29T16:26:03+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ColorMapperImpl implements ColorMapper {

    @Override
    public Color colorDtoToColor(ColorDto dto) {
        if ( dto == null ) {
            return null;
        }

        Color color = new Color();

        color.setName( dto.getName() );

        return color;
    }

    @Override
    public ColorDto colorToColorDto(Color color) {
        if ( color == null ) {
            return null;
        }

        ColorDto colorDto = new ColorDto();

        colorDto.setName( color.getName() );

        return colorDto;
    }
}
