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

    @GetMapping
    public String indexUserPage(Model model) {
        model.addAttribute("users", service.findAll());
        return "user";
    }

    @GetMapping("/{id}")
    public String editUser(@PathVariable(value = "id") Long id, Model model) {
        logger.info("Edit user with id {}", id);

        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("user", service.findById(id)
                .orElseThrow(NotFoundException::new));
        return "user_edit";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        logger.info("Add new user");

        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("systemUser", new SystemUser());
        return "user_new";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("systemUser") SystemUser systemUser,
                               BindingResult bindingResult,
                               Model model) {

        String userName = systemUser.getUsername();
        logger.debug("Processing registration form for: " + userName);
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "user_new";
        }

        if (service.findByUsername(userName).isPresent()) {
            model.addAttribute("systemUser", systemUser);
            model.addAttribute("registrationError", "Пользователь с таким именем уже существует!");
            logger.debug("User name already exist");
            model.addAttribute("roles", roleService.findAll());
            return "user_new";
        }
        service.addUser(systemUser);
        logger.debug("Successfully created user: " + userName);
        return "reg_confirmation";
    }

    @PostMapping("/update")
    public String updateUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "user_edit";
        }
        service.save(user);
        return "redirect:/user";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable(value = "id") Long id) {
        logger.info("Delete user with id {}", id);
        service.deleteById(id);
        return "redirect:/user";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

}










