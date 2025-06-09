package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.ColorDto;
import com.sunsophearin.shopease.entities.Color;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColorMapper {
    Color colorDtoToColor(ColorDto dto);
    ColorDto colorToColorDto(Color color);
}
