package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.ColorDto;
import com.sunsophearin.shopease.entities.Color;

public interface ColorService {
    Color createColor(ColorDto dto);
    Color getColorById(Long id);
}
