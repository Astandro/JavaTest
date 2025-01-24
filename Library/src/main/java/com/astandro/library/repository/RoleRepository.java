package com.astandro.library.repository;

import com.astandro.library.repository.entity.Role;
import com.astandro.library.repository.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
