package ru.stk.eshop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stk.eshop.entities.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
