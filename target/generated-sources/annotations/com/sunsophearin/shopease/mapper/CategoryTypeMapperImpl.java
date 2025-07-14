package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.CategoryTypeDto;
import com.sunsophearin.shopease.entities.CategoryType;
import com.sunsophearin.shopease.services.CategoryService;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-14T16:15:01+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class CategoryTypeMapperImpl implements CategoryTypeMapper {

    @Autowired
    private CategoryService categoryService;

    @Override
    public CategoryType toEntity(CategoryTypeDto dto) {
        if ( dto == null ) {
            return null;
        }

        CategoryType categoryType = new CategoryType();

        categoryType.setCategory( categoryService.getCategoryById( dto.getCategoryId() ) );
        categoryType.setName( dto.getName() );
        categoryType.setCode( dto.getCode() );
        categoryType.setDescription( dto.getDescription() );

        return categoryType;
    }
}
