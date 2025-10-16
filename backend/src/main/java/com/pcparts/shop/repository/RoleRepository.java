package com.pcparts.shop.repository;

import com.pcparts.shop.entity.Role;
import com.pcparts.shop.entity.Role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}


