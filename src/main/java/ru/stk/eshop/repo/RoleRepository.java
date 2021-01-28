package ru.stk.eshop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stk.eshop.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
