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
import ru.stk.eshop.utils.PriceFormatter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Shopping cart operations and logic
 */
@Getter
@Setter
@Component
//@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartService {
    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    private List<OrderItem> items;
    private String address;
    private String phone;
    private String displayDeliveryDate;
    private LocalDateTime deliveryDate;
    private BigDecimal totalCartPrice;
    private Integer totalCartQuantity;
    private String displayTotalCartPrice;

    public CartService() {
        items = new ArrayList<>(); //stores all OrderItems added to cart
        BigDecimal totalCost = new BigDecimal(0);
        totalCartQuantity = 0;
    }

    /**
     * add product to cart by product id
     *
     * @param productId - product id
     */
    public void addById(Long productId) {
        Product product = productService.findById(productId);
        this.add(product);
    }

    /**
     * add product to cart
     *
     * @param product - product object
     */
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
        recalculate();
    }

    /**
     * remove product from cart
     *
     * @param productId - product id
     */
    public void removeById(Long productId) {
        Product product = productService.findById(productId);
        this.remove(product);
    }

    /**
     * remove product from cart
     *
     * @param product - product
     */
    public void remove(Product product) {
        OrderItem orderItem = findOrderFromProduct(product);
        if (orderItem == null) {
            return;
        }
        if (orderItem.getQuantity() <= 1) {
            items.remove(orderItem);
        } else {
            orderItem.setQuantity(orderItem.getQuantity() - 1);
        }
        recalculate();
    }

    /**
     * cart recalculation
     */
    public void recalculate() {
        totalCartPrice = new BigDecimal(0);
        totalCartQuantity = 0;
        for (OrderItem o : items) {
            o.setTotalPrice(o.getProduct().getPrice().multiply(new BigDecimal(o.getQuantity())));
            o.setDisplayTotalPrice(PriceFormatter.format(o.getTotalPrice()));
            o.setDisplayPrice(PriceFormatter.format(o.getItemPrice()));
            totalCartPrice = totalCartPrice.add(o.getTotalPrice());
            totalCartQuantity = totalCartQuantity + o.getQuantity();
        }
        displayTotalCartPrice = PriceFormatter.format(totalCartPrice);
    }

    /**
     * cart reset
     */
    public void reset() {
        items = new ArrayList<>();
        BigDecimal totalCost = new BigDecimal(0);
        totalCartQuantity = 0;
    }

    /**
     * find order item by product
     *
     * @param product - product
     * @return order item
     */
    private OrderItem findOrderFromProduct(Product product) {
        return items.stream().filter(o -> o.getProduct().getId().equals(product.getId())).findFirst().orElse(null);
    }
}