package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.CategoryItemDto;
import com.sunsophearin.shopease.entities.CategoryItem;
import com.sunsophearin.shopease.entities.CategoryType;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-02T15:39:00+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class CategoryItemMapperImpl implements CategoryItemMapper {

    @Override
    public CategoryItemDto toDto(CategoryItem entity) {
        if ( entity == null ) {
            return null;
        }

        CategoryItemDto categoryItemDto = new CategoryItemDto();

        categoryItemDto.setCategoryTypeId( entityCategoryTypeId( entity ) );
        categoryItemDto.setName( entity.getName() );

        generateSlug( entity, categoryItemDto );

        return categoryItemDto;
    }

    @Override
    public CategoryItem toEntity(CategoryItemDto dto) {
        if ( dto == null ) {
            return null;
        }

        CategoryItem categoryItem = new CategoryItem();

        categoryItem.setCategoryType( mapCategoryTypeIdToCategoryType( dto.getCategoryTypeId() ) );
        categoryItem.setName( dto.getName() );

        return categoryItem;
    }

    @Override
    public List<CategoryItemDto> toDtoList(List<CategoryItem> list) {
        if ( list == null ) {
            return null;
        }

        List<CategoryItemDto> list1 = new ArrayList<CategoryItemDto>( list.size() );
        for ( CategoryItem categoryItem : list ) {
            list1.add( toDto( categoryItem ) );
        }

        return list1;
    }

    @Override
    public List<CategoryItem> toEntityList(List<CategoryItemDto> list) {
        if ( list == null ) {
            return null;
        }

        List<CategoryItem> list1 = new ArrayList<CategoryItem>( list.size() );
        for ( CategoryItemDto categoryItemDto : list ) {
            list1.add( toEntity( categoryItemDto ) );
        }

        return list1;
    }

    private Long entityCategoryTypeId(CategoryItem categoryItem) {
        CategoryType categoryType = categoryItem.getCategoryType();
        if ( categoryType == null ) {
            return null;
        }
        return categoryType.getId();
    }
}
