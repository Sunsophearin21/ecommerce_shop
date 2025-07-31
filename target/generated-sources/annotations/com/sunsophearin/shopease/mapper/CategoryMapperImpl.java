package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.CategoryDto;
import com.sunsophearin.shopease.entities.Category;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T16:55:13+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Autowired
    private CategoryTypeMapper categoryTypeMapper;

    @Override
    public Category categoryDtoToCategory(CategoryDto dto) {
        if ( dto == null ) {
            return null;
        }

        Category category = new Category();

        category.setCategoryTypes( categoryTypeMapper.toEntityList( dto.getCategoryTypes() ) );
        category.setId( dto.getId() );
        category.setName( dto.getName() );
        category.setDescription( dto.getDescription() );

        return category;
    }

    @Override
    public CategoryDto categoryToCategoryDto(Category entity) {
        if ( entity == null ) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setCategoryTypes( categoryTypeMapper.toDtoList( entity.getCategoryTypes() ) );
        categoryDto.setId( entity.getId() );
        categoryDto.setName( entity.getName() );
        categoryDto.setDescription( entity.getDescription() );

        generateSlug( entity, categoryDto );

        return categoryDto;
    }

    @Override
    public List<CategoryDto> toDtoList(List<Category> categories) {
        if ( categories == null ) {
            return null;
        }

        List<CategoryDto> list = new ArrayList<CategoryDto>( categories.size() );
        for ( Category category : categories ) {
            list.add( categoryToCategoryDto( category ) );
        }

        return list;
    }
}
