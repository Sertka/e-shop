package ru.stk.eshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.stk.eshop.entities.Order;
import ru.stk.eshop.entities.User;
import ru.stk.eshop.exceptions.NotFoundException;
import ru.stk.eshop.services.CartService;
import ru.stk.eshop.services.OrderService;
import ru.stk.eshop.services.UserService;
import ru.stk.eshop.utils.PriceFormatter;

import java.security.Principal;

/**
 *  MVC controller for orders
 */
@RequestMapping("/order")
@Controller
public class OrderController {
    private UserService userService;
    private OrderService orderService;
    private CartService cartService;
    private static final Logger logger = LoggerFactory.getLogger(ru.stk.eshop.controllers.ProductController.class);

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

    /**
     * all orders related to authenticated user
     * @param model - model
     * @param principal - principal
     * @return order list for authenticated user
     */
    @GetMapping("/list")
    public String orderList(Model model, Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("orders", orderService.getOrdersByUserId(user.getId()));
        model.addAttribute("cart", cartService);
        logger.info("Order list for user " + user.getUsername() + "is showed");
        return "order_list";
    }

    /**
     * creates new order after cart confirmation
     * @param model - model
     * @param principal - principal
     * @return order confirmation page
     */
    @GetMapping("/result")
    public String orderResult(Model model, Principal principal) {
        if (principal == null) {
            logger.info("Order is not created - login is required");
            return "redirect:/login";
        }

        User user = userService.findByUsername(principal.getName());
        Order order = orderService.makeOrder(cartService, user);
        order = orderService.saveOrder(order);

        order.setDisplayPrice(PriceFormatter.format(order.getPrice()));
        order.setDisplayDeliveryDate(cartService.getDisplayDeliveryDate());
        model.addAttribute("order", order);
        cartService.reset();
        logger.info("Order with id" + order.getId() + " is created");
        return "order_created";
    }

    /**
     * provides details of the specified order
     * @param id - order id
     * @param model - model
     * @param principal - principal
     * @return page with order details
     */
    @GetMapping("/{id}")
    public String orderDetails(@PathVariable(value = "id") Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(principal.getName());
        Order order = orderService.findById(id);

        //check if requested order belongs to authenticated user
        if (!user.getId().equals(order.getUser().getId())){
            return "not_found";
        }
        model.addAttribute("order", order);
        logger.info("Order with id {} is displayed", id);
        return "order_details";
    }

    /**
     * order admin page (used by shop employees)
     * @param model - model
     * @return order list with management functionality
     */
    @GetMapping("/admin")
    public String orderAdmin(Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        logger.info("Order admin page is displayed");
        return "order_admin";
    }

    /**
     * edit page for specified order
     * @param id - order id
     * @param model - model
     * @param principal - principal
     * @return order edit page
     */
    @GetMapping("/edit/{id}")
    public String editOrder(@PathVariable(value = "id") Long id, Model model, Principal principal) {
        model.addAttribute("statuses", orderService.getAllStatuses());
        model.addAttribute("order", orderService.findById(id));
        logger.info("Edit page for order with id {} is provided to user {}", id, principal.getName());
        return "order_edit";
    }

    /**
     * updates specified order
     * @param order - updated order
     * @param bindingResult binding result
     * @param model- model
     * @param principal  - principal
     * @return order admin page
     */
    @PostMapping("/update")
    public String updateOrder(Order order, BindingResult bindingResult, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", orderService.getAllStatuses());
            return "order_edit";
        }
        orderService.changeOrderStatus(order.getId(), order.getStatus());
        orderService.changeOrderAddress(order.getId(), order.getAddress());
        orderService.changeOrderPhone(order.getId(), order.getPhone());
        orderService.setOrderUpdateDate(order.getId());
        logger.info("Order with id {} is updated by user {}", order.getId(), principal.getName());
        return "redirect:/order/admin";
    }

    /**
     * deletes specified order
     * @param id - order id
     * @param principal - principal
     * @return order admin page
     */
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable(value = "id") Long id, Principal principal) {
        orderService.deleteById(id);
        logger.info("Order with id {} is deleted by user {}", id, principal.getName());
        return "redirect:/order/admin";
    }

    @ExceptionHandler
    private ModelAndView notFoundExceptionHandler(NotFoundException e){
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}