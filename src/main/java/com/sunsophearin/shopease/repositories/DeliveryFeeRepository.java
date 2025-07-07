package com.sunsophearin.shopease.repositories;

import com.sunsophearin.shopease.entities.DeliveryFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryFeeRepository extends JpaRepository<DeliveryFee,Long> {
}
