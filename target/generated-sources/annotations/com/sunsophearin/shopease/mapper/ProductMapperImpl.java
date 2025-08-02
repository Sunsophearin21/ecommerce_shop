package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.MenuFactureDto;
import com.sunsophearin.shopease.dto.ProductDto;
import com.sunsophearin.shopease.dto.response.ProductDtoRespone;
import com.sunsophearin.shopease.entities.MenuFacturer;
import com.sunsophearin.shopease.entities.Product;
import com.sunsophearin.shopease.entities.ProductVariant;
import com.sunsophearin.shopease.services.MenuFacturerService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-02T15:38:59+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryTypeMapper categoryTypeMapper;
    @Autowired
    private CategoryItemMapper categoryItemMapper;
    @Autowired
    private MenuFacturerService menuFacturerService;

    @Override
    public Product productDtoToProduct(ProductDto dto) {
        if ( dto == null ) {
            return null;
        }

        Product product = new Product();

        product.setCategoryType( map( dto.getCategoryTypeId() ) );
        product.setCategory( categoryTypeMapper.map( dto.getCategoryId() ) );
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

        productDtoRespone.setCategory( categoryMapper.categoryToCategoryDto( product.getCategory() ) );
        productDtoRespone.setCategoryType( categoryTypeMapper.toDto( product.getCategoryType() ) );
        productDtoRespone.setCategoryItem( categoryItemMapper.toDto( product.getCategoryItem() ) );
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
        productDtoRespone.setDeliveryFee( product.getDeliveryFee() );
        productDtoRespone.setCreateAt( product.getCreateAt() );
        productDtoRespone.setUpdateAt( product.getUpdateAt() );
        productDtoRespone.setNewArrival( product.isNewArrival() );

        productDtoRespone.setFinalPrice( product.getFinalPrice() );

        setSlug( product, productDtoRespone );

        return productDtoRespone;
    }

    @Override
    public List<ProductDtoRespone> toDtoList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductDtoRespone> list = new ArrayList<ProductDtoRespone>( products.size() );
        for ( Product product : products ) {
            list.add( toDtoList( product ) );
        }

        return list;
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
