package com.sunsophearin.shopease.repositories;

import com.sunsophearin.shopease.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Long> {
    String findByTransactionId(String transactionId);
}
