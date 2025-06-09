package com.sunsophearin.shopease.services.impl;

import com.sunsophearin.shopease.dto.ColorDto;
import com.sunsophearin.shopease.entities.Color;
import com.sunsophearin.shopease.exception.ResoureApiNotFound;
import com.sunsophearin.shopease.mapper.ColorMapper;
import com.sunsophearin.shopease.repositories.ColorRepository;
import com.sunsophearin.shopease.services.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;
    @Override
    public Color createColor(ColorDto dto) {
        Color color = colorMapper.colorDtoToColor(dto);
        return colorRepository.save(color);
    }

    @Override
    public Color getColorById(Long id) {
        return colorRepository.findById(id).orElseThrow(()->new ResoureApiNotFound("Color",id));
    }
}
