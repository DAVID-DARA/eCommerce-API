package com.project.ecommerce_api.repositories;

import com.project.ecommerce_api.entities.Role;
import com.project.ecommerce_api.utilities.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName (RoleEnum name);
}
