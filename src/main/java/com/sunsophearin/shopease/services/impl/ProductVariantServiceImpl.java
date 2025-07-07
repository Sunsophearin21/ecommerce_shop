package com.sunsophearin.shopease.services.impl;

import com.sunsophearin.shopease.dto.ProductVariantDto;
import com.sunsophearin.shopease.entities.*;
import com.sunsophearin.shopease.exception.ResoureApiNotFound;
import com.sunsophearin.shopease.mapper.ProductVariantMapper;
import com.sunsophearin.shopease.repositories.*;
import com.sunsophearin.shopease.services.ProductService;
import com.sunsophearin.shopease.services.ProductVariantService;
import com.sunsophearin.shopease.services.UploadImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
    private final ProductVariantRepository productVariantRepository;
    private final UploadImageFileService uploadImageFileService;
    private final ProductVariantMapper productVariantMapper;
    @Autowired
    @Lazy
    private ProductService productService;


    @Override
    public ProductVariant getById(Long id) {
        return productVariantRepository.findById(id).orElseThrow(()->new ResoureApiNotFound("ProductVariant",id));
    }

    @Override
    public ProductVariant update(Long id, ProductVariantDto dto) {
//        ProductVariant productVariant = getById(id); // assumes this throws if not found
//        if (dto.getColorId() != null) {
//            Color color = colorRepository.findById(dto.getColorId())
//                    .orElseThrow(() -> new RuntimeException("Color not found"));
//            productVariant.setColor(color);
//        }
//
//        if (dto.getSizeId() != null) {
//            Size size = sizeRepository.findById(dto.getSizeId())
//                    .orElseThrow(() -> new RuntimeException("Size not found"));
//            productVariant.setSize(size);
//        }
//
//        if (dto.getResourcesId() != null) {
//            Resources resources = resourcesRepository.findById(dto.getResourcesId())
//                    .orElseThrow(() -> new RuntimeException("Resources not found"));
//            productVariant.setResources(resources);
//        }
//
//        if (dto.getStockQuantity() != null) {
//            productVariant.setStockQuantity(dto.getStockQuantity());
//        }

        return productVariantRepository.save(null);
    }

    @Override
    public ProductVariant createProductVariant(ProductVariantDto dto, MultipartFile[] files) throws IOException {
        List<String> uploadImages = uploadImageFileService.uploadImages(files);
        ProductVariant productVariant = productVariantMapper.productVariantDtoToProductVariant(dto);
        Product product = productService.getProductById(dto.getProductId());
        productVariant.setProduct(product);
        productVariant.setImages(uploadImages);
        return productVariantRepository.save(productVariant);
    }
}
