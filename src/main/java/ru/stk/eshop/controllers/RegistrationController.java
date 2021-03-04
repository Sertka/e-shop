package ru.stk.eshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.stk.eshop.entities.SystemUser;
import ru.stk.eshop.services.RoleService;
import ru.stk.eshop.services.UserService;

import javax.validation.Valid;

/**
 * MVC registration controller for new client
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(ru.stk.eshop.controllers.ProductController.class);
    private RoleService roleService;
    private UserService userService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * prepare and call client registration form
     * @param model - model
     * @return client registration form
     */
    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("systemUser", new SystemUser());
        logger.info("Register request for new client");
        return "user_new";
    }


    /**
     * @param systemUser
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String registerUser(@Valid @ModelAttribute("systemUser") SystemUser systemUser,
                               BindingResult bindingResult,
                               Model model) {

        String username = systemUser.getUsername();
        logger.debug("Processing registration form for: " + username);
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "user_new";
        }

        if (userService.usernameExist(username)) {
            model.addAttribute("systemUser", systemUser);
            model.addAttribute("registrationError", "Пользователь с таким именем уже существует!");
            model.addAttribute("roles", roleService.findAll());
            logger.info("User name {} already exist", username);
            return "user_new";
        }
        userService.addUser(systemUser);
        logger.info("Successfully created user {}", username);
        return "reg_confirmation";
    }
}