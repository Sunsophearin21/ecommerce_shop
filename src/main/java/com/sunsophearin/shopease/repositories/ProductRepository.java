package com.sunsophearin.shopease.repositories;

import com.sunsophearin.shopease.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByCategoryTypeId(Long categoryTypeId);
    List<Product> findByCategoryItemId(Long categoryItemId);
}
