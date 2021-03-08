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
import ru.stk.eshop.services.CartService;
import ru.stk.eshop.services.RoleService;
import ru.stk.eshop.services.UserService;
import ru.stk.eshop.utils.UserPassword;

import javax.validation.Valid;
import java.security.Principal;

/**
 * MVC profile controller. Allows user to register, change her/his profile and password.
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private UserService userService;
    private RoleService roleService;
    private CartService cartService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
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
     * save new user
     * @param systemUser system user - used for user profile check and creation
     * @param bindingResult - binding result
     * @param model - model
     * @return registration confirmation
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

    /**
     * prepare and open form for user profile edit
     * @param id - user id
     * @param model - model
     * @param principal - principal
     * @return user profile edit form
     */
    @GetMapping("/{id}")
    public String editUser(@PathVariable(value = "id") Long id, Model model, Principal principal) {
        //security check (user changes has own data)
        User currentUser  = userService.findByUsername(principal.getName());
        if (!userService.checkCurrentUserValidity(id, currentUser)){
            throw new NotFoundException();
        }

        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("user", userService.findById(id));
        logger.info("Edit user with id {} by {}", id, principal.getName());
        return "profile_edit";
    }

    /**
     * user profile update
     * @param user = user
     * @param bindingResult - binding result
     * @param model - model
     * @return if errors - profile edit form, if update successful, main page
     */
    @PostMapping("/update")
    public String updateUser(@Valid User user, BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "profile_edit";
        }
        userService.save(user);
        logger.info("User profile is updated by user {}", user.getUsername());
        return "redirect:/";
    }

    /**
     * password edit
     * @param id - user id
     * @param model - model
     * @param principal - principal
     * @return password edit form
     */
    @GetMapping("/password/{id}")
    public String editPassword(@PathVariable(value = "id") Long id, Model model, Principal principal) {
        //security check (user changes has own data)
        User currentUser  = userService.findByUsername(principal.getName());
        if (!userService.checkCurrentUserValidity(id, currentUser)){
            throw new NotFoundException();
        }

        UserPassword password = new UserPassword();
        password.setUserId(id);
        model.addAttribute("password", password);
        logger.info("User {} password change form is displayed for user {}", password.getUserId(), principal.getName());
        return "password_edit";
    }

    /**
     * password update
     * @param password - new password
     * @param bindingResult - binding result
     * @param model - model
     * @param principal - principal
     * @return login form for users / user edit form for administrator
     */
    @PostMapping("/password/update")
    public String updatePassword(@Valid UserPassword password, BindingResult bindingResult,
                                 Model model, Principal principal) {
        String page = "/login";
        if (bindingResult.hasErrors()) {
            model.addAttribute("password", password);
            return "password_edit";
        }
        userService.updatePassword(password);
        logger.info("Change user password for user_id {} by user {}", password.getUserId(), principal.getName());

        // define the return page (admin -> 'user_edit', user -> '/login'"
        User user = userService.findByUsername(principal.getName());
        Role r = user.getRoles().stream()
                .filter(role -> "ROLE_ADMIN".equals(role.getName()))
                .findAny()
                .orElse(null);

        if (r != null){
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("user", userService.findById(password.getUserId()));
            page = "user_edit";
        }
        return page;
    }

    @ExceptionHandler
    private ModelAndView notFoundExceptionHandler(NotFoundException e){
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        logger.error("Resource not found exception");
        return modelAndView;
    }
}