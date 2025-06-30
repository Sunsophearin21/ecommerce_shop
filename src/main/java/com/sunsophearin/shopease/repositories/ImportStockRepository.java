package com.sunsophearin.shopease.repositories;

import com.sunsophearin.shopease.entities.ImportStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportStockRepository extends JpaRepository<ImportStock, Long> {
    // You can add custom queries if needed later
}
