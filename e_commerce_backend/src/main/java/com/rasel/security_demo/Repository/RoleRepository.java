package com.rasel.security_demo.Repository;

import com.rasel.security_demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Role.ERole name);
}
