package ru.stk.eshop.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stk.eshop.entities.Product;
import ru.stk.eshop.entities.SystemUser;
import ru.stk.eshop.entities.User;
import ru.stk.eshop.exceptions.NotFoundException;
import ru.stk.eshop.repo.RoleRepository;
import ru.stk.eshop.repo.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public List<User> findAll(){
        return repo.findAll();
    }

    public User findById(Long userId) {
        if (repo.findById(userId).isPresent()) {
            return repo.findById(userId).get();
        } else throw new NotFoundException();
    }

    public User findByUsername(String username) {
        if (repo.findByUsername(username).isPresent()) {
            return repo.findByUsername(username).get();
        } else{
            logger.warn("User " + username + " does not found!");
            throw new NotFoundException();
        }
    }

    public Boolean usernameExist(String username) {
        return repo.findByUsername(username).isPresent();
    }

    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repo.save(user);
    }

    @Transactional
    public boolean addUser(SystemUser systemUser) {
        User user = new User();

        if (repo.findByUsername(systemUser.getUsername()).isPresent()) {
            return false;
        }

        user.setUsername(systemUser.getUsername());
        user.setPassword(passwordEncoder.encode(systemUser.getPassword()));
        user.setFirstName(systemUser.getFirstName());
        user.setLastName(systemUser.getLastName());
        user.setPhone(systemUser.getPhone());
        user.setAddress(systemUser.getAddress());
        user.setEmail(systemUser.getEmail());

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

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

}