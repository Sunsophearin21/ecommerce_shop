package com.sunsophearin.shopease.services.impl;

import com.sunsophearin.shopease.dto.MenuFactureDto;
import com.sunsophearin.shopease.entities.MenuFacturer;
import com.sunsophearin.shopease.exception.ResoureApiNotFound;
import com.sunsophearin.shopease.mapper.MenuFacturerMapper;
import com.sunsophearin.shopease.repositories.MenuFactureRepository;
import com.sunsophearin.shopease.services.MenuFacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuFacturerServiceImpl implements MenuFacturerService {
    private final MenuFactureRepository menuFactureRepository;
    private final MenuFacturerMapper menuFacturerMapper;
    @Override
    public MenuFacturer create(MenuFactureDto dto) {
        return menuFactureRepository.save(menuFacturerMapper.menuFacturerDtoToMenuFacturer(dto));
    }

    @Override
    public MenuFacturer getMenuFacById(Long id) {
        return menuFactureRepository.findById(id).orElseThrow(()->new ResoureApiNotFound("MenuFacturer",id));
    }
}
