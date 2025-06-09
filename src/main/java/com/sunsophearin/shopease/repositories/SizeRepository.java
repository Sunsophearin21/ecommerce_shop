package com.sunsophearin.shopease.repositories;

import com.sunsophearin.shopease.entities.Product;
import com.sunsophearin.shopease.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size,Long>, JpaSpecificationExecutor<Product> {
}
