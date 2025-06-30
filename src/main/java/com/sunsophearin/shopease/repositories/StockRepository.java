package com.sunsophearin.shopease.repositories;

import com.sunsophearin.shopease.entities.ProductVariant;
import com.sunsophearin.shopease.entities.Size;
import com.sunsophearin.shopease.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // Find stock by variant and size (for checking duplicate constraint or updating)
    Optional<Stock> findByProductVariantAndSize(ProductVariant variant, Size size);

}
