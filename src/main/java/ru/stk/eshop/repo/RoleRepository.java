package ru.stk.eshop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stk.eshop.entities.Role;
import ru.stk.eshop.entities.User;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
