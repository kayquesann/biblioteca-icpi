package com.biblioteca_icpi.repository;

import com.biblioteca_icpi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Long, Role> {
    Role findByNome(String nome);
}
