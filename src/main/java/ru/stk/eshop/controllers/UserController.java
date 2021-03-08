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
import ru.stk.eshop.entities.Role;
import ru.stk.eshop.entities.SystemUser;
import ru.stk.eshop.entities.User;
import ru.stk.eshop.exceptions.NotFoundException;
import ru.stk.eshop.services.RoleService;
import ru.stk.eshop.services.UserService;
import ru.stk.eshop.utils.UserPassword;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

/**
 *  MVC controller for user management
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
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
        model.addAttribute("users", userService.findAll());
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
        model.addAttribute("user", userService.findById(id));
        logger.info("Edit user with id {} by {}", id, principal.getName());
        return "user_edit";
    }

     /**
     * new user form
     * @param model - model
     * @param principal - principald
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

    /**
     * user profile update by administrator
     * @param user edited user
     * @param bindingResult - binding result
     * @param model - model
     * @return user list
     */
    @PostMapping("/update")
    public String updateUser(@Valid User user, BindingResult bindingResult,
                             Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "user_edit";
        }
        userService.save(user);
        logger.info("User {} profile is updated by administrator {}", user.getId(), principal.getName());
        return "redirect:/user/admin";
    }

    /**
     * delete user profile
     * @param id - deleted user id
     * @param principal - principal
     * @return user list
     */
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable(value = "id") Long id, Principal principal) {
        logger.info("Delete user with id {}", id);
        userService.deleteById(id);
        logger.info("User {} profile is deleted by administrator {}", id, principal.getName());
        return "redirect:/user/admin";
    }

    @ExceptionHandler
    private ModelAndView notFoundExceptionHandler(NotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        logger.error("Resource not found exception");
        return modelAndView;
    }
}