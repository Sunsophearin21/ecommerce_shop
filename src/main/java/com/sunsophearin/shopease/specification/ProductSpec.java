package com.sunsophearin.shopease.specification;

import com.sunsophearin.shopease.entities.Product;
import com.sunsophearin.shopease.entities.ProductVariant;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProductSpec implements Specification<Product> {
    private final productFilter2 productFilter;
    @Override
    public Predicate toPredicate(Root<Product> productRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // Basic product filters
        if (productFilter.getId() != null) {
            predicates.add(productRoot.get("id").in(productFilter.getId()));
        }

        // Category filters
        if (productFilter.getCategory() != null) {
            predicates.add(productRoot.get("category").get("id").in(productFilter.getCategory()));
        }

        if (productFilter.getCategoryType() != null) {
            predicates.add(productRoot.get("categoryType").get("id").in(productFilter.getCategoryType()));
        }

        // Manufacturer filter
        if (productFilter.getMenuFacturer() != null) {
            predicates.add(productRoot.get("menuFacturer").get("id").in(productFilter.getMenuFacturer()));
        }

        // Size filter
        if (productFilter.getSize() != null) {
            predicates.add(productRoot.get("size").get("id").in(productFilter.getSize()));
        }

        // Color filter through product variants
        if (productFilter.getColor() != null) {
            Join<Product, ProductVariant> variantJoin = productRoot.join("productVariants");
            predicates.add(variantJoin.get("color").get("id").in(productFilter.getColor()));

            // Ensure distinct results when joining
            query.distinct(true);
        }

        // Product variant filter
        if (productFilter.getProductVariant() != null) {
            Join<Product, ProductVariant> variantJoin = productRoot.join("productVariants");
            predicates.add(variantJoin.get("id").in(productFilter.getProductVariant()));

            // Ensure distinct results when joining
            query.distinct(true);
        }

        // Price range filter (example addition)
        if (productFilter.getMinPrice() != null) {
            predicates.add(cb.greaterThanOrEqualTo(productRoot.get("price"), productFilter.getMinPrice()));
        }

        if (productFilter.getMaxPrice() != null) {
            predicates.add(cb.lessThanOrEqualTo(productRoot.get("price"), productFilter.getMaxPrice()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}