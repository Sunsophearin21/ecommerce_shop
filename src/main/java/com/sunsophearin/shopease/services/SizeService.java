package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.ColorDto;
import com.sunsophearin.shopease.dto.SizeDto;
import com.sunsophearin.shopease.entities.Color;
import com.sunsophearin.shopease.entities.Size;

public interface SizeService {
    Size createSizw(SizeDto dto);
    Size getSizeById(Long id);
}
