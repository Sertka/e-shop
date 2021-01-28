package ru.stk.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stk.eshop.entities.User;
import ru.stk.eshop.repo.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository repo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository (UserRepository repo){
        this.repo = repo;
    }

    @Autowired
    public void setPasswordEncoder (PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll(){
        return repo.findAll();
    }

    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repo.save(user);
    }

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

}
