package com.biblioteca_icpi.repository;

import com.biblioteca_icpi.model.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AluguelRepository extends JpaRepository <Aluguel, Long> {
}
