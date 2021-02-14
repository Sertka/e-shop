package ru.stk.eshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.stk.eshop.services.BrandService;
import ru.stk.eshop.services.CartService;
import ru.stk.eshop.services.ProductService;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(ru.stk.eshop.controllers.ProductController.class);
    private ProductService productService;
    private BrandService brandService;
    private CartService cartService;

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

    @GetMapping
    public String indexProductPage(Model model,
                                   @RequestParam(name = "nameFilter") Optional<String> nameFilter,
                                   @RequestParam(name = "minFilter") Optional<BigDecimal> minFilter,
                                   @RequestParam(name = "maxFilter") Optional<BigDecimal> maxFilter,
                                   @RequestParam(name = "page") Optional<Integer> page,
                                   @RequestParam(name = "size") Optional<Integer> size,
                                   @RequestParam(name = "brand") Optional<Integer> brand,
                                   @RequestParam(name = "sortField") Optional<String> sortField,
                                   @RequestParam(name = "changeSortOrder") Optional<Boolean> changeSortOrder) {

        logger.info("Index page update");


        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("cart", cartService);
        model.addAttribute("products", productService.findWithFilter(nameFilter,
                minFilter,
                maxFilter,
                page,
                size,
                brand,
                sortField,
                changeSortOrder));
        return "index";
    }


}
