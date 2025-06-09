package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.ResourcesDto;
import com.sunsophearin.shopease.entities.Resources;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResourcesMapper {
    @Mapping(source = "imageUrls", target = "images")
    Resources toEntity(ResourcesDto dto);

    @Mapping(source = "images", target = "imageUrls")
    ResourcesDto toDto(Resources entity);
}
