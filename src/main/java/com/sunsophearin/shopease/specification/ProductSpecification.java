package com.sunsophearin.shopease.specification;

import com.sunsophearin.shopease.entities.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProductSpecification implements Specification<Product> {
    private final ProductFilter productFilter;

    @Override
    public Predicate toPredicate(Root<Product> productRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if(productFilter.getId() != null) {
            predicates.add(productRoot.get("id").in(productFilter.getId()));
        }

        if(productFilter.getCategory() != null) {
            // Compare category ID instead of whole Category object
            predicates.add(productRoot.get("category").get("id").in(productFilter.getCategory()));
        }

        if(productFilter.getCategoryType() != null) {
            // Compare categoryType ID instead of whole CategoryType object
            predicates.add(productRoot.get("categoryType").get("id").in(productFilter.getCategoryType()));
        }

        if(productFilter.getMenuFacturer() != null) {
            // Compare manufacturer ID instead of whole Manufacturer object
            predicates.add(productRoot.get("menuFacturer").get("id").in(productFilter.getMenuFacturer()));
        }

        if(productFilter.getColor() != null) {
            predicates.add(productRoot.get("color").get("id").in(productFilter.getColor()));
        }

        if(productFilter.getSize() != null) {
            predicates.add(productRoot.get("size").get("id").in(productFilter.getSize()));
        }
        if(productFilter.getProductVariant()!=null){
            predicates.add(productRoot.get("productVariant").get("id").in(productFilter.getProductVariant()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}