package com.biblioteca_icpi.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "TB_ALUGUEL")
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "livro_id")
    private Livro livro;

    private LocalDate dataInicio;

    private LocalDate dataDevolucao;

}
