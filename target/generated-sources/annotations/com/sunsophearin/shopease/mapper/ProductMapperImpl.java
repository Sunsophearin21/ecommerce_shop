package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.CategoryDto;
import com.sunsophearin.shopease.dto.CategoryTypeDto;
import com.sunsophearin.shopease.dto.MenuFactureDto;
import com.sunsophearin.shopease.dto.ProductDto;
import com.sunsophearin.shopease.dto.ProductDtoRespone;
import com.sunsophearin.shopease.entities.Category;
import com.sunsophearin.shopease.entities.CategoryType;
import com.sunsophearin.shopease.entities.MenuFacturer;
import com.sunsophearin.shopease.entities.Product;
import com.sunsophearin.shopease.entities.ProductVariant;
import com.sunsophearin.shopease.services.CategoryService;
import com.sunsophearin.shopease.services.CategoryTypeService;
import com.sunsophearin.shopease.services.MenuFacturerService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-29T16:26:04+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryTypeService categoryTypeService;
    @Autowired
    private MenuFacturerService menuFacturerService;

    @Override
    public Product productDtoToProduct(ProductDto dto) {
        if ( dto == null ) {
            return null;
        }

        Product product = new Product();

        product.setCategoryType( categoryTypeService.getCategoryTypeById( dto.getCategoryTypeId() ) );
        product.setCategory( categoryService.getCategoryById( dto.getCategoryId() ) );
        product.setMenuFacturer( menuFacturerService.getMenuFacById( dto.getMenuFacturerId() ) );
        product.setName( dto.getName() );
        product.setDescription( dto.getDescription() );
        product.setPrice( dto.getPrice() );
        product.setNewArrival( dto.isNewArrival() );

        return product;
    }

    @Override
    public ProductDtoRespone toDtoList(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDtoRespone productDtoRespone = new ProductDtoRespone();

        productDtoRespone.setCategory( categoryToCategoryDto( product.getCategory() ) );
        productDtoRespone.setCategoryType( categoryTypeToCategoryTypeDto( product.getCategoryType() ) );
        productDtoRespone.setMenuFacturer( menuFacturerToMenuFactureDto( product.getMenuFacturer() ) );
        List<ProductVariant> list = product.getProductVariants();
        if ( list != null ) {
            productDtoRespone.setProductVariants( new ArrayList<ProductVariant>( list ) );
        }
        productDtoRespone.setDiscount( product.getDiscount() );
        productDtoRespone.setId( product.getId() );
        productDtoRespone.setName( product.getName() );
        productDtoRespone.setDescription( product.getDescription() );
        productDtoRespone.setPrice( product.getPrice() );
        productDtoRespone.setCreateAt( product.getCreateAt() );
        productDtoRespone.setUpdateAt( product.getUpdateAt() );
        productDtoRespone.setNewArrival( product.isNewArrival() );

        productDtoRespone.setFinalPrice( product.getFinalPrice() );

        return productDtoRespone;
    }

    protected CategoryDto categoryToCategoryDto(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setName( category.getName() );
        categoryDto.setCode( category.getCode() );
        categoryDto.setDescription( category.getDescription() );

        return categoryDto;
    }

    protected CategoryTypeDto categoryTypeToCategoryTypeDto(CategoryType categoryType) {
        if ( categoryType == null ) {
            return null;
        }

        CategoryTypeDto categoryTypeDto = new CategoryTypeDto();

        categoryTypeDto.setName( categoryType.getName() );
        categoryTypeDto.setCode( categoryType.getCode() );
        categoryTypeDto.setDescription( categoryType.getDescription() );

        return categoryTypeDto;
    }

    protected MenuFactureDto menuFacturerToMenuFactureDto(MenuFacturer menuFacturer) {
        if ( menuFacturer == null ) {
            return null;
        }

        MenuFactureDto menuFactureDto = new MenuFactureDto();

        menuFactureDto.setName( menuFacturer.getName() );

        return menuFactureDto;
    }
}
