package ru.stk.eshop.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stk.eshop.entities.Role;
import ru.stk.eshop.entities.SystemUser;
import ru.stk.eshop.entities.User;
import ru.stk.eshop.exceptions.NotFoundException;
import ru.stk.eshop.repo.RoleRepository;
import ru.stk.eshop.repo.UserRepository;
import ru.stk.eshop.utils.UserPassword;

import java.util.Collections;
import java.util.List;

/**
 * User entity operations
 */
@Service
public class UserService {

    private UserRepository repo;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(ru.stk.eshop.controllers.ProductController.class);

    @Autowired
    public void setUserRepository (UserRepository repo){
        this.repo = repo;
    }
    @Autowired
    public void setRoleRepository (RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }
    @Autowired
    public void setPasswordEncoder (PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * find all existed users
     * @return user list
     */
    public List<User> findAll(){
        return repo.findAll();
    }

    /**
     * find user by id
     * @param userId - user id
     * @return user or NotFound exception
     */
    public User findById(Long userId) {
        if (repo.findById(userId).isPresent()) {
            return repo.findById(userId).get();
        } else{
            logger.warn("User with id {} not found!", userId);
            throw new NotFoundException();
        }
    }

    /**
     * find user by username
     * @param username - username
     * @return - user or NotFound exception
     */
    public User findByUsername(String username) {
        if (repo.findByUsername(username).isPresent()) {
            return repo.findByUsername(username).get();
        } else{
            logger.warn("User {} not found!", username);
            throw new NotFoundException();
        }
    }

    /**
     * check if user exists
     * @param username - username
     * @return - true / false
     */
    public Boolean usernameExist(String username) {
        return repo.findByUsername(username).isPresent();
    }

    /**
     * save user in DB
     * @param user - user
     */
    @Transactional
    public void save(User user) {
        if (user.getPassword().length() != 60) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        repo.save(user);
    }

    /**
     * update user password
     * @param password - new password
     */
    @Transactional
    public void updatePassword(UserPassword password) {
        User user = findById(password.getUserId());
        user.setPassword(passwordEncoder.encode(password.getPassword()));
        repo.save(user);
    }

    /**
     * save new user to DB
     * @param systemUser - system user
     * @return saving status (true / false)
     */
    @Transactional
    public boolean addUser(SystemUser systemUser) {
        User user = new User();

        if (repo.findByUsername(systemUser.getUsername()).isPresent()) {
            return false;
        }

        //fill in user fields
        user.setUsername(systemUser.getUsername());
        user.setPassword(passwordEncoder.encode(systemUser.getPassword()));
        user.setFirstName(systemUser.getFirstName());
        user.setLastName(systemUser.getLastName());
        user.setPhone(systemUser.getPhone());
        user.setAddress(systemUser.getAddress());
        user.setEmail(systemUser.getEmail());

        //defining user roles (client / employee)
        if(systemUser.getRoles() != null){
            //new company user
            user.setRoles(systemUser.getRoles());
        } else{
            //new client
            user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_CLIENT")));
        }

        repo.save(user);
        return true;
    }

    /**
     * delete user
     * @param id - user id
     */
    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    /**
     * checks if user, who wants to change profile
     * changes her/his own profile (admins can change profiles for all users)
     * @param id - user id
     * @param user - current user
     * @return changes validity (true / false)
     */
    public Boolean checkCurrentUserValidity(Long id, User user){

        Role r = user.getRoles().stream()
                .filter(role -> "ROLE_ADMIN".equals(role.getName()))
                .findAny()
                .orElse(null);

        if (r == null){
            //if not admin
            return id.equals(user.getId());
        } else{
            //if user has 'ADMIN' rights
            return true;
        }
    }
}