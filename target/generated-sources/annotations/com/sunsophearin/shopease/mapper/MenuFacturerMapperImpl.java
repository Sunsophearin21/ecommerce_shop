package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.MenuFactureDto;
import com.sunsophearin.shopease.entities.MenuFacturer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-15T01:13:59+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class MenuFacturerMapperImpl implements MenuFacturerMapper {

    @Override
    public MenuFacturer menuFacturerDtoToMenuFacturer(MenuFactureDto dto) {
        if ( dto == null ) {
            return null;
        }

        MenuFacturer menuFacturer = new MenuFacturer();

        menuFacturer.setName( dto.getName() );

        return menuFacturer;
    }
}
