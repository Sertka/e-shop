package ru.stk.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stk.eshop.entities.Order;
import ru.stk.eshop.entities.OrderItem;
import ru.stk.eshop.entities.OrderStatus;
import ru.stk.eshop.entities.User;
import ru.stk.eshop.repo.OrderRepository;
import ru.stk.eshop.utils.PriceFormatter;
import ru.stk.eshop.utils.ReceiverApp;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order makeOrder(CartService cart, User user) {
        Order order = new Order();
        order.setId(0L);
        order.setUser(user);
        order.setStatus(OrderStatus.RECEIVED);
        order.setPrice(cart.getTotalPrice());
        order.setOrderItems(new ArrayList<>(cart.getItems()));
        order.setAddress (cart.getAddress());
        order.setPhone(cart.getPhone());
        order.setPrintDeliveryDate(cart.getPrintDeliveryDate());

        for (OrderItem o : order.getOrderItems()) {
            o.setOrder(order);
        }
        order.setPrintPrice(PriceFormatter.format(order.getPrice()));

        //RabbitMQ receive
        try {
            ReceiverApp.receive();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return order;
    }

    public List<Order> getAllOrders() {
        return (List<Order>) orderRepository.findAll();
    }

    public Order findById(Long id) {
        if (orderRepository.findById(id).isPresent())
        return orderRepository.findById(id).get();
        else{
            return null;
        }
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order changeOrderStatus(Order order, OrderStatus newStatus) {
        order.setStatus(newStatus);
        return saveOrder(order);
    }
}
