package ru.stk.eshop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stk.eshop.entities.Brand;
import ru.stk.eshop.entities.Type;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
}
