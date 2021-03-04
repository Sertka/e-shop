package ru.stk.eshop.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stk.eshop.entities.Order;
import ru.stk.eshop.entities.OrderItem;
import ru.stk.eshop.entities.OrderStatus;
import ru.stk.eshop.entities.User;
import ru.stk.eshop.exceptions.NotFoundException;
import ru.stk.eshop.repo.OrderRepository;
import ru.stk.eshop.utils.PriceFormatter;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private static final Logger logger = LoggerFactory.getLogger(ru.stk.eshop.controllers.ProductController.class);

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order makeOrder(CartService cart, User user) {
        Order order = new Order();
        order.setId(0L);
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setPrice(cart.getTotalCartPrice());
        order.setOrderItems(new ArrayList<>(cart.getItems()));
        order.setPhone(cart.getPhone());
        order.setAddress(cart.getAddress());
        order.setCreateDate(LocalDateTime.now());
        order.setUpdateDate(LocalDateTime.now());
        order.setDeliveryDate(cart.getDeliveryDate());
        for (OrderItem o : order.getOrderItems()) {
            o.setOrder(order);
        }
        fillDisplayFields(order);


        return order;
    }

    public List<Order> getAllOrders() {
        List<Order> list = orderRepository.findAll();
        for (Order order: list){
            fillDisplayFields(order);
        }
        return list;
    }

    public List<Order> getOrdersByUserId(Long UserId) {
        List<Order> list = orderRepository.findOrderByUserId(UserId);
        for (Order order: list){
            fillDisplayFields(order);
        }
        return list;
    }

    public Order findById(Long id) {
        Order order;
        if (orderRepository.findById(id).isPresent()){
            order = orderRepository.findById(id).get();
            fillDisplayFields(order);
            return order;
        }else{
            throw new NotFoundException();
        }
    }

    public Order saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        fillDisplayFields(savedOrder);
        return savedOrder;
    }

    public void changeOrderStatus(Long id, OrderStatus newStatus) {
        Order order = findById(id);
        order.setStatus(newStatus);
        Order savedOrder = orderRepository.save(order);
        fillDisplayFields(savedOrder);
    }

    public void changeOrderAddress(Long id, String newAddress) {
        Order order = findById(id);
        order.setAddress(newAddress);
        Order savedOrder = orderRepository.save(order);
        fillDisplayFields(savedOrder);
    }

    public void changeOrderPhone(Long id, String newPhone) {
        Order order = findById(id);
        order.setPhone(newPhone);
        Order savedOrder = orderRepository.save(order);
        fillDisplayFields(savedOrder);
    }

    public void setOrderUpdateDate(Long id) {
        Order order = findById(id);
        order.setUpdateDate(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        fillDisplayFields(savedOrder);
    }


    private void fillDisplayFields(Order order){
        order.setDisplayPrice(PriceFormatter.format(order.getPrice()));
        order.setDisplayDeliveryDate(order.getDeliveryDate().toLocalDate().toString());
        order.setDisplayCreateDate(order.getCreateDate().toLocalDate().toString());
        order.setDisplayUpdateDate(order.getUpdateDate().toLocalDate().toString());

        for (OrderItem o: order.getOrderItems()){
            o.setDisplayPrice(PriceFormatter.format(o.getItemPrice()));
            o.setDisplayTotalPrice(PriceFormatter.format(o.getTotalPrice()));
        }
    }

    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }


    public ArrayList<OrderStatus> getAllStatuses(){
        ArrayList<OrderStatus> statuses = new ArrayList<>();
        Collections.addAll(statuses, OrderStatus.values());
        return statuses;
    }
}
