package ru.stk.eshop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stk.eshop.entities.Brand;
import ru.stk.eshop.entities.Type;

public interface TypeRepository extends JpaRepository<Type, Long> {
}
