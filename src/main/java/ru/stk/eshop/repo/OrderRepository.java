package ru.stk.eshop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stk.eshop.entities.Order;
import ru.stk.eshop.entities.OrderStatus;
import ru.stk.eshop.entities.Product;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrderByUserId(Long id);

}
