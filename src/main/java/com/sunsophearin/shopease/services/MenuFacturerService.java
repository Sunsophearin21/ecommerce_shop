package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.MenuFactureDto;
import com.sunsophearin.shopease.entities.MenuFacturer;

public interface MenuFacturerService {
    MenuFacturer create(MenuFactureDto dto);
    MenuFacturer getMenuFacById(Long id);
}
