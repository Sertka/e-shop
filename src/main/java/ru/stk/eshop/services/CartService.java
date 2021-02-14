package ru.stk.eshop.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ru.stk.eshop.entities.OrderItem;
import ru.stk.eshop.entities.Product;
import ru.stk.eshop.exceptions.NotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartService {
    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    private List<OrderItem> items;
    private BigDecimal totalCost;
    private Integer totalQuantity;

    public CartService() {
        items = new ArrayList<>();
        BigDecimal totalCost = new BigDecimal(0);
        totalQuantity = 0;
    }

    public void addById(Long productId) {
        if (productService.findById(productId).isPresent()) {
            Product product = productService.findById(productId).get();
            this.add(product);
        } else throw new NotFoundException();
    }

    public void add(Product product) {
        OrderItem orderItem = findOrderFromProduct(product);
        if (orderItem == null) {
            orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setItemPrice(product.getPrice());
            orderItem.setQuantity(0);
            orderItem.setId(0L);
            orderItem.setTotalPrice(new BigDecimal(0));
            items.add(orderItem);
        }
        orderItem.setQuantity(orderItem.getQuantity() + 1);
        //recalculate();
    }

    public void setQuantity(Product product, Integer quantity) {
        OrderItem orderItem = findOrderFromProduct(product);
        if (orderItem == null) {
            return;
        }
        orderItem.setQuantity(quantity);
        recalculate();
    }

    public void remove(Product product) {
        OrderItem orderItem = findOrderFromProduct(product);
        if (orderItem == null) {
            return;
        }
        items.remove(orderItem);
        recalculate();
    }

    public void recalculate() {
        totalCost = new BigDecimal(0);
        totalQuantity = 0;
        for (OrderItem o : items) {
            o.setTotalPrice(o.getProduct().getPrice().multiply(new BigDecimal(o.getQuantity())));
            totalCost = totalCost.add(o.getTotalPrice());
            totalQuantity = totalQuantity + o.getQuantity();
        }
    }

    private OrderItem findOrderFromProduct(Product product) {
        return items.stream().filter(o -> o.getProduct().getId().equals(product.getId())).findFirst().orElse(null);
    }
}
