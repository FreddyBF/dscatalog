package com.github.freddy.dscatalog.repository;

import com.github.freddy.dscatalog.model.Role;
import com.github.freddy.dscatalog.model.RoleAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByAuthority(RoleAuthority authority);
}
