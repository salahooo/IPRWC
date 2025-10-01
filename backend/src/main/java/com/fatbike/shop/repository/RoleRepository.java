package com.fatbike.shop.repository;

import com.fatbike.shop.entity.Role;
import com.fatbike.shop.entity.Role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
