package ru.stk.eshop.repo;

import org.springframework.data.jpa.domain.Specification;
import ru.stk.eshop.entities.Product;

import java.math.BigDecimal;

public class ProductSpec {

    public static Specification<Product> nameLike(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Product> priceBigger(BigDecimal minPrice) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> priceLess(BigDecimal maxPrice) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

}