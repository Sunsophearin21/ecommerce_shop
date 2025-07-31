package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.CategoryTypeDto;
import com.sunsophearin.shopease.entities.Category;
import com.sunsophearin.shopease.entities.CategoryType;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = CategoryItemMapper.class)
public interface CategoryTypeMapper {

    // ✅ Mapping ពី DTO → Entity
    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "categoryItems", source = "categoryItems")  // ✅ Map DTO List → Entity List
    CategoryType toEntity(CategoryTypeDto dto);

    // ✅ Mapping ពី Entity → DTO
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryItems", source = "categoryItems")  // ✅ Map Entity List → DTO List
    CategoryTypeDto toDto(CategoryType entity);

    List<CategoryTypeDto> toDtoList(List<CategoryType> entities);
    List<CategoryType> toEntityList(List<CategoryTypeDto> dtos);

    // ✅ បំលែង categoryId ➜ Category entity (for setting FK during save)
    default Category map(Long categoryId) {
        if (categoryId == null) return null;
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    // ✅ បង្កើត slug បន្ទាប់ពី mapping Entity ➜ DTO
    @AfterMapping
    default void setSlug(CategoryType entity, @MappingTarget CategoryTypeDto dto) {
        String name = entity.getName() != null ? entity.getName() : "";
        String categoryName = (entity.getCategory() != null && entity.getCategory().getName() != null)
                ? entity.getCategory().getName()
                : "";
        Long id = entity.getId() != null ? entity.getId() : 0L;

        // Convert names to slug-friendly format
        String nameSlug = name.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
        String categorySlug = categoryName.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");

        String slug = categorySlug + "-" + nameSlug + "-" + id;
        dto.setSlug(slug);
    }
}
