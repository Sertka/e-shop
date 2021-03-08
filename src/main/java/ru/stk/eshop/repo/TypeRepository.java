package ru.stk.eshop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stk.eshop.entities.ProductType;

@Repository
public interface TypeRepository extends JpaRepository<ProductType, Long> {
}
