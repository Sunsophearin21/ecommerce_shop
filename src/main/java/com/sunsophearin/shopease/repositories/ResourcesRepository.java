package com.sunsophearin.shopease.repositories;

import com.sunsophearin.shopease.entities.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourcesRepository extends JpaRepository<Resources,Long> {
}
