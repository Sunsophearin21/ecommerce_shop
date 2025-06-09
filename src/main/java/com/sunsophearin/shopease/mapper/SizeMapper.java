package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.ColorDto;
import com.sunsophearin.shopease.dto.SizeDto;
import com.sunsophearin.shopease.entities.Color;
import com.sunsophearin.shopease.entities.Size;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SizeMapper {
    Size SizeDtoToSize(SizeDto dto);
}
