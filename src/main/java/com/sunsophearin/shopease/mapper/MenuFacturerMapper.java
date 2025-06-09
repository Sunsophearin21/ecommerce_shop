package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.ColorDto;
import com.sunsophearin.shopease.dto.MenuFactureDto;
import com.sunsophearin.shopease.entities.Color;
import com.sunsophearin.shopease.entities.MenuFacturer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuFacturerMapper {
    MenuFacturer menuFacturerDtoToMenuFacturer(MenuFactureDto dto);
}
