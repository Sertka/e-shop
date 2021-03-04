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
import ru.stk.eshop.entities.SystemUser;
import ru.stk.eshop.entities.User;
import ru.stk.eshop.exceptions.NotFoundException;
import ru.stk.eshop.services.RoleService;
import ru.stk.eshop.services.UserService;

import javax.validation.Valid;
import java.security.Principal;

/**
 *  MVC controller for users
 */
@RequestMapping("/user")
@Controller
public class UserController {

    private UserService service;
    private RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public void setUserService(UserService service) {
        this.service = service;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * user list for administrators
     * @param model - model
     * @return user list
     */
    @GetMapping("/admin")
    public String indexUserPage(Model model) {
        model.addAttribute("users", service.findAll());
        logger.info("User list admin page is displayed");
        return "user";
    }

    /**
     * user edit form preparation
     * @param id - user id
     * @param model - model
     * @param principal - principal
     * @return user edit form
     */
    @GetMapping("/{id}")
    public String editUser(@PathVariable(value = "id") Long id, Model model, Principal principal) {
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("user", service.findById(id));
        logger.info("Edit user with id {} by {}", id, principal.getName());
        return "user_edit";
    }

    /**
     * new user form
     * @param model - model
     * @param principal - principal
     * @return new user form
     */
    @GetMapping("/new")
    public String newUser(Model model, Principal principal) {
        logger.info("Add new user");
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("systemUser", new SystemUser());
        logger.info("New user form is opened by {}", principal.getName());
        return "user_new";
    }

    @PostMapping("/update")
    public String updateUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "user_edit";
        }
        service.save(user);
        return "redirect:/user/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable(value = "id") Long id) {
        logger.info("Delete user with id {}", id);
        service.deleteById(id);
        return "redirect:/user/admin";
    }

    @ExceptionHandler
    private ModelAndView notFoundExceptionHandler(NotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

}










