package ru.stk.eshop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stk.eshop.entities.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
