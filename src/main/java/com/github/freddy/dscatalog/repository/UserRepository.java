package com.github.freddy.dscatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.freddy.dscatalog.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
