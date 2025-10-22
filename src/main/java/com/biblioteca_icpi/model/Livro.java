package com.biblioteca_icpi.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "TB_LIVRO")
public class Livro {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    private String autor;

    private boolean disponivel = true;

    @OneToMany(mappedBy = "livro")
    private List<Aluguel> alugueis;
}
