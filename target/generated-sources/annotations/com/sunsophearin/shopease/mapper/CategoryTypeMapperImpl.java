package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.CategoryTypeDto;
import com.sunsophearin.shopease.entities.Category;
import com.sunsophearin.shopease.entities.CategoryType;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-31T17:48:50+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class CategoryTypeMapperImpl implements CategoryTypeMapper {

    @Autowired
    private CategoryItemMapper categoryItemMapper;

    @Override
    public CategoryType toEntity(CategoryTypeDto dto) {
        if ( dto == null ) {
            return null;
        }

        CategoryType categoryType = new CategoryType();

        categoryType.setCategory( map( dto.getCategoryId() ) );
        categoryType.setCategoryItems( categoryItemMapper.toEntityList( dto.getCategoryItems() ) );
        categoryType.setName( dto.getName() );
        categoryType.setCode( dto.getCode() );
        categoryType.setDescription( dto.getDescription() );

        return categoryType;
    }

    @Override
    public CategoryTypeDto toDto(CategoryType entity) {
        if ( entity == null ) {
            return null;
        }

        CategoryTypeDto categoryTypeDto = new CategoryTypeDto();

        categoryTypeDto.setCategoryId( entityCategoryId( entity ) );
        categoryTypeDto.setCategoryItems( categoryItemMapper.toDtoList( entity.getCategoryItems() ) );
        categoryTypeDto.setName( entity.getName() );
        categoryTypeDto.setCode( entity.getCode() );
        categoryTypeDto.setDescription( entity.getDescription() );

        setSlug( entity, categoryTypeDto );

        return categoryTypeDto;
    }

    @Override
    public List<CategoryTypeDto> toDtoList(List<CategoryType> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CategoryTypeDto> list = new ArrayList<CategoryTypeDto>( entities.size() );
        for ( CategoryType categoryType : entities ) {
            list.add( toDto( categoryType ) );
        }

        return list;
    }

    @Override
    public List<CategoryType> toEntityList(List<CategoryTypeDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<CategoryType> list = new ArrayList<CategoryType>( dtos.size() );
        for ( CategoryTypeDto categoryTypeDto : dtos ) {
            list.add( toEntity( categoryTypeDto ) );
        }

        return list;
    }

    private Long entityCategoryId(CategoryType categoryType) {
        Category category = categoryType.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getId();
    }
}
