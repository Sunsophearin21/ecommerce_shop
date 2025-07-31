package com.sunsophearin.shopease.mapper;

import com.sunsophearin.shopease.dto.response.SaleDetailDtoResponse;
import com.sunsophearin.shopease.dto.response.SaleDtoResponse;
import com.sunsophearin.shopease.entities.Color;
import com.sunsophearin.shopease.entities.Product;
import com.sunsophearin.shopease.entities.ProductVariant;
import com.sunsophearin.shopease.entities.Sale;
import com.sunsophearin.shopease.entities.SaleDetail;
import com.sunsophearin.shopease.entities.Size;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-31T15:46:55+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class SaleMapperImpl implements SaleMapper {

    @Override
    public SaleDtoResponse saleToSaleDto(Sale sale) {
        if ( sale == null ) {
            return null;
        }

        SaleDtoResponse saleDtoResponse = new SaleDtoResponse();

        saleDtoResponse.setId( sale.getId() );
        saleDtoResponse.setOrderId( sale.getOrderId() );
        saleDtoResponse.setTransactionId( sale.getTransactionId() );
        saleDtoResponse.setStatus( sale.getStatus() );
        if ( sale.getDeliveryStatus() != null ) {
            saleDtoResponse.setDeliveryStatus( sale.getDeliveryStatus().name() );
        }
        saleDtoResponse.setNotes( sale.getNotes() );
        saleDtoResponse.setQuantity( sale.getQuantity() );
        saleDtoResponse.setFinalPrice( sale.getFinalPrice() );
        saleDtoResponse.setPhoneNumber( sale.getPhoneNumber() );
        saleDtoResponse.setAddress( sale.getAddress() );
        saleDtoResponse.setLatitude( sale.getLatitude() );
        saleDtoResponse.setLongitude( sale.getLongitude() );
        saleDtoResponse.setCreateAt( sale.getCreateAt() );
        saleDtoResponse.setUpdateAt( sale.getUpdateAt() );
        saleDtoResponse.setSaleDetails( saleDetailsToSaleDetailDtos( sale.getSaleDetails() ) );

        return saleDtoResponse;
    }

    @Override
    public List<SaleDtoResponse> salesToSaleDtos(List<Sale> sales) {
        if ( sales == null ) {
            return null;
        }

        List<SaleDtoResponse> list = new ArrayList<SaleDtoResponse>( sales.size() );
        for ( Sale sale : sales ) {
            list.add( saleToSaleDto( sale ) );
        }

        return list;
    }

    @Override
    public List<SaleDetailDtoResponse> saleDetailsToSaleDetailDtos(List<SaleDetail> saleDetails) {
        if ( saleDetails == null ) {
            return null;
        }

        List<SaleDetailDtoResponse> list = new ArrayList<SaleDetailDtoResponse>( saleDetails.size() );
        for ( SaleDetail saleDetail : saleDetails ) {
            list.add( saleDetailToSaleDetailDto( saleDetail ) );
        }

        return list;
    }

    @Override
    public SaleDetailDtoResponse saleDetailToSaleDetailDto(SaleDetail saleDetail) {
        if ( saleDetail == null ) {
            return null;
        }

        SaleDetailDtoResponse saleDetailDtoResponse = new SaleDetailDtoResponse();

        saleDetailDtoResponse.setProductName( saleDetailProductName( saleDetail ) );
        Long id = saleDetailProductVariantId( saleDetail );
        if ( id != null ) {
            saleDetailDtoResponse.setProductVariantName( String.valueOf( id ) );
        }
        saleDetailDtoResponse.setSizeName( saleDetailSizeName( saleDetail ) );
        saleDetailDtoResponse.setColorName( saleDetailColorName( saleDetail ) );
        List<String> images = saleDetailProductVariantImages( saleDetail );
        List<String> list = images;
        if ( list != null ) {
            saleDetailDtoResponse.setImages( new ArrayList<String>( list ) );
        }
        saleDetailDtoResponse.setId( saleDetail.getId() );
        saleDetailDtoResponse.setPrice( saleDetail.getPrice() );
        saleDetailDtoResponse.setDiscount( saleDetail.getDiscount() );
        saleDetailDtoResponse.setFinalPrice( saleDetail.getFinalPrice() );
        saleDetailDtoResponse.setQuantity( saleDetail.getQuantity() );

        return saleDetailDtoResponse;
    }

    private String saleDetailProductName(SaleDetail saleDetail) {
        Product product = saleDetail.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private Long saleDetailProductVariantId(SaleDetail saleDetail) {
        ProductVariant productVariant = saleDetail.getProductVariant();
        if ( productVariant == null ) {
            return null;
        }
        return productVariant.getId();
    }

    private String saleDetailSizeName(SaleDetail saleDetail) {
        Size size = saleDetail.getSize();
        if ( size == null ) {
            return null;
        }
        return size.getName();
    }

    private String saleDetailColorName(SaleDetail saleDetail) {
        Color color = saleDetail.getColor();
        if ( color == null ) {
            return null;
        }
        return color.getName();
    }

    private List<String> saleDetailProductVariantImages(SaleDetail saleDetail) {
        ProductVariant productVariant = saleDetail.getProductVariant();
        if ( productVariant == null ) {
            return null;
        }
        return productVariant.getImages();
    }
}
