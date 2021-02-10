package ru.stk.eshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.stk.eshop.entities.SystemUser;
import ru.stk.eshop.services.RoleService;

@Controller
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(ru.stk.eshop.controllers.ProductController.class);
    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/register")
    public String newUser(Model model) {
        logger.info("Add new user");

        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("systemUser", new SystemUser());
        return "user_new";


    }
}
