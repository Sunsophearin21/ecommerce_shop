package com.sunsophearin.shopease.repositories;

import com.sunsophearin.shopease.entities.MenuFacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuFactureRepository extends JpaRepository<MenuFacturer,Long> {
}
