package com.sunsophearin.shopease.repositories;

import com.sunsophearin.shopease.entities.Category;
import com.sunsophearin.shopease.entities.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryTypeRepository extends JpaRepository<CategoryType,Long> {
}
