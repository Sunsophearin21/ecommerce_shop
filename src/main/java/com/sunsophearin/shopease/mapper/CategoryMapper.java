package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.CategoryDto;
import com.sunsophearin.shopease.entities.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryTypeMapper.class})
public interface CategoryMapper {

    @Mapping(target = "categoryTypes", source = "categoryTypes")
    Category categoryDtoToCategory(CategoryDto dto);

    @Mapping(target = "categoryTypes", source = "categoryTypes")
    CategoryDto categoryToCategoryDto(Category entity);

    // Mapping List
    List<CategoryDto> toDtoList(List<Category> categories);

    // បង្កើត slug បន្ទាប់ពី Mapping ចប់
    @AfterMapping
    default void generateSlug(Category entity, @MappingTarget CategoryDto dto) {
        if (entity.getName() != null) {
            String slug = entity.getName()
                    .toLowerCase()
                    .replaceAll("[^a-z0-9]+", "-")
                    .replaceAll("(^-|-$)", "");
            dto.setSlug(slug); // ឧ. "men"
        }
    }
}
