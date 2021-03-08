package ru.stk.eshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.stk.eshop.entities.User;
import ru.stk.eshop.exceptions.NotFoundException;
import ru.stk.eshop.services.BrandService;
import ru.stk.eshop.services.CartService;
import ru.stk.eshop.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;

/**
 * MVC controller for a shopping cart
 */
@Controller
@RequestMapping("/cart")
public class CartController {
    private UserService userService;
    private CartService cartService;
    private BrandService brandService;
    private final int DELIVERY_SHIFT = 3; //amount of days from order confirmation till estimated delivery

    private static final Logger logger = LoggerFactory.getLogger(ru.stk.eshop.controllers.ProductController.class);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }

    /**
     * main cart page
     * @param model - model
     * @return refreshed page with goods in the cart
     */
    @GetMapping
    public String cartPage(Model model) {
        cartService.recalculate(); //recalculates prices and quantities in the cart
        model.addAttribute("cart", cartService);
        model.addAttribute("brands", brandService.findAll());
        logger.info("Cart page is refreshed");
        return "cart";
    }

    /**
     * after cart is filled in and button "Make order" is pressed, this confirmation form is showed
     * @param model - model
     * @param principal - principal
     * @return cart confirmation page
     */
    @GetMapping("/confirm")
    public String cartConfirm(Model model, Principal principal) {

        //cart can be confirmed only if the user logged in
        if (principal == null) {
            logger.info("Cart is not confirmed - login is required");
            return "redirect:/login";
        }
        User user = userService.findByUsername(principal.getName());

        //fill in cart properties
        cartService.setAddress (user.getAddress());
        cartService.setPhone(user.getPhone());
        cartService.setDeliveryDate(LocalDateTime.now().plusDays(DELIVERY_SHIFT));
        cartService.setDisplayDeliveryDate(cartService.getDeliveryDate().toLocalDate().toString());
        model.addAttribute("cart", cartService);
        logger.info("Cart is confirmation is called");
        return "cart_confirm";
    }

    /**
     * add new product to cart
     * @param id - product id
     * @param httpServletRequest
     * @return the page from the request was initiated
     */
    @GetMapping("/add/{id}")
    public String addProductToCart(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        cartService.addById(id);
        String referrer = httpServletRequest.getHeader("referer");
        logger.info("New product with id" + id + " added to cart");
        return "redirect:" + referrer;
    }

    /**
     * remove product from cart
     * @param id - product id
     * @param httpServletRequest
     * @return the page from the request was initiated
     */
    @GetMapping("/remove/{id}")
    public String removeProductFromCart(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        cartService.removeById(id);
        String referrer = httpServletRequest.getHeader("referer");
        logger.info("Product with id" + id + " removed from cart");
        return "redirect:" + referrer;
    }

    @ExceptionHandler
    private ModelAndView notFoundExceptionHandler(NotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        logger.error("Resource not found exception");
        return modelAndView;
    }
}