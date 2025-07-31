package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.*;
import com.sunsophearin.shopease.dto.response.ProductDtoRespone;
import com.sunsophearin.shopease.entities.*;
import com.sunsophearin.shopease.services.*;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", uses = {
        CategoryMapper.class,
        CategoryTypeMapper.class,
        CategoryItemMapper.class,
        MenuFacturerService.class,
        ProductVariantService.class, // ✅ already here
        ProductVariantMapper.class
})
public interface ProductMapper {

    // ---------- Create: DTO → Entity ----------
    @Mapping(target = "categoryType", source = "categoryTypeId")
    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "menuFacturer", source = "menuFacturerId")
    Product productDtoToProduct(ProductDto dto);

    // ---------- Entity → DTO Response ----------
    @Mapping(target = "category", source = "category")
    @Mapping(target = "categoryType", source = "categoryType")
    @Mapping(target = "categoryItem", source = "categoryItem")
    @Mapping(target = "menuFacturer", source = "menuFacturer")
    @Mapping(target = "productVariants", source = "productVariants")
    @Mapping(target = "discount", source = "discount")
    @Mapping(target = "finalPrice", expression = "java(product.getFinalPrice())")
    // 👇 Don't map slug here; do it in @AfterMapping!
    ProductDtoRespone toDtoList(Product product);

    // ---------- Optionally: List Mapping ----------
    List<ProductDtoRespone> toDtoList(List<Product> products);

    // ---------- Add Slug After Mapping ----------
    @AfterMapping
    default void setSlug(Product product, @MappingTarget ProductDtoRespone dto) {
        // Defensive: handle nulls and edge cases!
        if (product.getName() != null && product.getId() != null) {
            String namePart = product.getName()
                    .toLowerCase()
                    .replaceAll("[^a-z0-9]+", "-")
                    .replaceAll("(^-|-$)", "");
            dto.setSlug(namePart + "-" + product.getId());
        } else {
            dto.setSlug(null); // or just ID or empty string
        }
    }
    default CategoryType map(Long categoryTypeId) {
        if (categoryTypeId == null) return null;
        CategoryType categoryType = new CategoryType();
        categoryType.setId(categoryTypeId);
        return categoryType;
    }
}
