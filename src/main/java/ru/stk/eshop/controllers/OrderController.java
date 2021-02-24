package ru.stk.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.stk.eshop.entities.Order;
import ru.stk.eshop.entities.User;
import ru.stk.eshop.services.CartService;
import ru.stk.eshop.services.OrderService;
import ru.stk.eshop.services.UserService;
import ru.stk.eshop.utils.PriceFormatter;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class OrderController {
    private UserService userService;
    private OrderService orderService;
    private CartService cartService;
    private final int DELIVERY_SHIFT = 3;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setShoppingCart(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/order_confirm")
    public String orderConfirm(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        if (userService.findByUsername(principal.getName()).isPresent()) {
            User user = userService.findByUsername(principal.getName()).get();

            cartService.setAddress (user.getAddress());
            cartService.setPhone(user.getPhone());
            cartService.setPrintDeliveryDate(LocalDateTime.now().plusDays(DELIVERY_SHIFT).toLocalDate().toString());
            model.addAttribute("cart", cartService);

            return "order_confirm";
        } else{
            return "redirect:/login";
        }
    }

    @GetMapping("/order_result")
    public String orderResult(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        if (userService.findByUsername(principal.getName()).isPresent()) {
            User user = userService.findByUsername(principal.getName()).get();
            Order order = orderService.makeOrder(cartService, user);
            order.setDeliveryDate(LocalDateTime.now().plusDays(DELIVERY_SHIFT));
            order = orderService.saveOrder(order);

            order.setPrintPrice(PriceFormatter.format(order.getPrice()));
            order.setPrintDeliveryDate(cartService.getPrintDeliveryDate());
            model.addAttribute("order", order);
            cartService.reset();
            return "order_created";
        }else {
            return "redirect:/login";
        }

    }
}
