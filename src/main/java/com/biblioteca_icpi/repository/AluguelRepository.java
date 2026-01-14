package com.biblioteca_icpi.repository;

import com.biblioteca_icpi.model.Aluguel;
import com.biblioteca_icpi.model.Livro;
import com.biblioteca_icpi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AluguelRepository extends JpaRepository <Aluguel, Long> {
    boolean existsByLivro(Livro livro);
    boolean existsByUsuario (Usuario usuario);
    List<Aluguel> findByUsuario (Usuario usuario);
}
