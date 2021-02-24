package ru.stk.eshop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stk.eshop.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
