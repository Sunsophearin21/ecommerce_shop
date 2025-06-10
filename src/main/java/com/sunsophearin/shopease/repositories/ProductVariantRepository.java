package com.sunsophearin.shopease.repositories;

import com.sunsophearin.shopease.entities.Product;
import com.sunsophearin.shopease.entities.ProductVariant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant,Long>{
    Optional<ProductVariant> findByProductIdAndColorId(Long productId, Long colorId);
    @EntityGraph(attributePaths = {"stocks", "stocks.size"})
    @Query("SELECT v FROM ProductVariant v WHERE v.id = :variantId")
    Optional<ProductVariant> findByIdWithStocks(@Param("variantId") Long variantId);
}
