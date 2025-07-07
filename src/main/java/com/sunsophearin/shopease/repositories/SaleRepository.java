package com.sunsophearin.shopease.repositories;

import com.sunsophearin.shopease.entities.Sale;
import com.sunsophearin.shopease.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Long> {
    String findByTransactionId(String transactionId);
    List<Sale> findByUserIdAndDeliveryStatus(Long userId, DeliveryStatus deliveryStatus);
}
