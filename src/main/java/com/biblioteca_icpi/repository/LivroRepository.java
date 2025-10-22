package com.biblioteca_icpi.repository;

import com.biblioteca_icpi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro,Long> {
}
