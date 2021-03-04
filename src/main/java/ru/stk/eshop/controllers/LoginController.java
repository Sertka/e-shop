package ru.stk.eshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * MVC controller for login page
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(ru.stk.eshop.controllers.ProductController.class);
    /**
     * shows login page
     * @return login page
     */
    @GetMapping("/login")
    public String showMyLoginPage() {
        logger.info("Login page showed");
        return "login";
    }
}