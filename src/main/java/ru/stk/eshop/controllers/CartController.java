package ru.stk.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.stk.eshop.services.BrandService;
import ru.stk.eshop.services.CartService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/cart")
public class CartController {
    private CartService cartService;
    private BrandService brandService;

    @Autowired
    public void setShoppingCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public String cartPage(Model model) {
        model.addAttribute("cart", cartService);
        model.addAttribute("brands", brandService.findAll());
        return "cart";
    }

    @GetMapping("/add/{id}")
    public String addProductToCart(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        cartService.addById(id);
        String referrer = httpServletRequest.getHeader("referer");
        return "redirect:" + referrer;
    }
}
