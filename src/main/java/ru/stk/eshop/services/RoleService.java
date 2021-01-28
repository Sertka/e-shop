package ru.stk.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stk.eshop.entities.Role;
import ru.stk.eshop.repo.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private RoleRepository repo;

    @Autowired
    public void setRoleRepository (RoleRepository repo){
        this.repo = repo;
    }

    public List<Role> findAll(){
        return repo.findAll();
    }

    public Optional<Role> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public void save(Role role) {
        repo.save(role);
    }

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

}
