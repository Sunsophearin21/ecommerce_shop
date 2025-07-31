package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.CategoryItemDto;
import com.sunsophearin.shopease.entities.CategoryItem;
import com.sunsophearin.shopease.entities.CategoryType;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryItemMapper {

    // Entity → DTO
    @Mapping(target = "categoryTypeId", source = "categoryType.id")
    @Mapping(target = "slug", ignore = true) // Slug will be set manually
    CategoryItemDto toDto(CategoryItem entity);

    // DTO → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryType", source = "categoryTypeId", qualifiedByName = "mapCategoryTypeIdToCategoryType")
    CategoryItem toEntity(CategoryItemDto dto);

    List<CategoryItemDto> toDtoList(List<CategoryItem> list);
    List<CategoryItem> toEntityList(List<CategoryItemDto> list);

    // ✅ Convert Long to CategoryType (for ID binding during save)
    @Named("mapCategoryTypeIdToCategoryType")
    default CategoryType mapCategoryTypeIdToCategoryType(Long id) {
        if (id == null) return null;
        CategoryType type = new CategoryType();
        type.setId(id);
        return type;
    }

    // ✅ Generate slug like "new-in-top-1"
    @AfterMapping
    default void generateSlug(CategoryItem entity, @MappingTarget CategoryItemDto dto) {
        if (entity.getName() == null || entity.getCategoryType() == null) {
            return;
        }

        String itemName = entity.getName();
        String typeName = entity.getCategoryType().getName();
        Long id = entity.getId();

        if (itemName == null || typeName == null) return;

        String itemSlug = itemName.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
        String typeSlug = typeName.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");

        // Combine type + item + id
        String slug = typeSlug + "-" + itemSlug + (id != null ? "-" + id : "");

        dto.setSlug(slug);
    }
}
