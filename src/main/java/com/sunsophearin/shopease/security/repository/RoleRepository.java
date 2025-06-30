package com.sunsophearin.shopease.security.repository;

import com.sunsophearin.shopease.security.entities.Role;
import com.sunsophearin.shopease.security.entities.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
