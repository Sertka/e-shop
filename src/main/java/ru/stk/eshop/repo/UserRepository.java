package ru.stk.eshop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stk.eshop.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUsersByUsername(String username);
}
