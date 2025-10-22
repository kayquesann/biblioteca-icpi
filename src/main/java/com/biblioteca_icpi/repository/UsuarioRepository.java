package com.biblioteca_icpi.repository;

import com.biblioteca_icpi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
