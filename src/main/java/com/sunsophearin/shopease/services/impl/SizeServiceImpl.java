package com.sunsophearin.shopease.services.impl;

import com.sunsophearin.shopease.dto.ColorDto;
import com.sunsophearin.shopease.dto.SizeDto;
import com.sunsophearin.shopease.entities.Color;
import com.sunsophearin.shopease.entities.Size;
import com.sunsophearin.shopease.exception.ResoureApiNotFound;
import com.sunsophearin.shopease.mapper.ColorMapper;
import com.sunsophearin.shopease.mapper.SizeMapper;
import com.sunsophearin.shopease.repositories.ColorRepository;
import com.sunsophearin.shopease.repositories.SizeRepository;
import com.sunsophearin.shopease.services.ColorService;
import com.sunsophearin.shopease.services.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;
    @Override
    public Size createSizw(SizeDto dto) {
        return sizeRepository.save(sizeMapper.SizeDtoToSize(dto));
    }

    @Override
    public Size getSizeById(Long id) {
        return sizeRepository.findById(id).orElseThrow(()->new ResoureApiNotFound("Size",id));
    }
}
