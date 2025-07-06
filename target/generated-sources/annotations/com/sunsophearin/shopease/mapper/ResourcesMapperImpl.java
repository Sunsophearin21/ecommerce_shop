package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.ResourcesDto;
import com.sunsophearin.shopease.entities.Resources;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-06T14:32:35+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ResourcesMapperImpl implements ResourcesMapper {

    @Override
    public Resources toEntity(ResourcesDto dto) {
        if ( dto == null ) {
            return null;
        }

        Resources resources = new Resources();

        List<String> list = dto.getImageUrls();
        if ( list != null ) {
            resources.setImages( new ArrayList<String>( list ) );
        }
        resources.setType( dto.getType() );

        return resources;
    }

    @Override
    public ResourcesDto toDto(Resources entity) {
        if ( entity == null ) {
            return null;
        }

        ResourcesDto resourcesDto = new ResourcesDto();

        List<String> list = entity.getImages();
        if ( list != null ) {
            resourcesDto.setImageUrls( new ArrayList<String>( list ) );
        }
        resourcesDto.setType( entity.getType() );

        return resourcesDto;
    }
}
