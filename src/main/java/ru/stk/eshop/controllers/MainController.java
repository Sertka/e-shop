package ru.stk.eshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.stk.eshop.services.BrandService;
import ru.stk.eshop.services.CartService;
import ru.stk.eshop.services.ProductService;
import ru.stk.eshop.services.UserService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

/**
 * MVC controller for main shop page
 */
@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(ru.stk.eshop.controllers.ProductController.class);
    private ProductService productService;
    private BrandService brandService;
    private CartService cartService;
    private UserService userService;

    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * refreshes main shop page
     * @param model - model
     * @param nameFilter - product name
     * @param minFilter - min price
     * @param maxFilter - max price
     * @param page - page number
     * @param size - lines on the page
     * @param brand - brand name
     * @param sortField - sorting by field
     * @param changeSortOrder - change sort order flag (asc/dsc)
     * @return refreshed main page
     */
    @GetMapping
    public String indexProductPage(Model model, Principal principal,
                                   @RequestParam(name = "nameFilter") Optional<String> nameFilter,
                                   @RequestParam(name = "minFilter") Optional<BigDecimal> minFilter,
                                   @RequestParam(name = "maxFilter") Optional<BigDecimal> maxFilter,
                                   @RequestParam(name = "page") Optional<Integer> page,
                                   @RequestParam(name = "size") Optional<Integer> size,
                                   @RequestParam(name = "brand") Optional<Integer> brand,
                                   @RequestParam(name = "sortField") Optional<String> sortField,
                                   @RequestParam(name = "changeSortOrder") Optional<Boolean> changeSortOrder) {

        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("cart", cartService);
        if (principal != null && !principal.getName().equals("")) {
            model.addAttribute("user", userService.findByUsername(principal.getName()));
        }
        model.addAttribute("products", productService.findWithFilter(nameFilter,
                minFilter,
                maxFilter,
                page,
                size,
                brand,
                sortField,
                changeSortOrder));
        logger.info("Main shop page is refreshed");
        return "index";
    }
}