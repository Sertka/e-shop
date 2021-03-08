package ru.stk.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stk.eshop.entities.Role;
import ru.stk.eshop.repo.RoleRepository;

import java.util.List;
import java.util.Optional;

/**
 * Role entity operations
 */
@Service
public class RoleService {

    private RoleRepository repo;

    @Autowired
    public void setRoleRepository (RoleRepository repo){
        this.repo = repo;
    }

    /**
     * find all existed roles
     * @return role list
     */
    public List<Role> findAll(){
        return repo.findAll();
    }

    /**
     * find role by id
     * @param id - role id
     * @return role (optional)
     */
    public Optional<Role> findById(Long id) {
        return repo.findById(id);
    }

    /**
     * save role in DB
     * @param role - role
     */
    @Transactional
    public void save(Role role) {
        repo.save(role);
    }

    /**
     * delete role from db
     * @param id - role id
     */
    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}