package com.biblioteca_icpi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "TB_LIVRO")
public class Livro {

    public Livro() {
    }

    public Livro(Long id, String nome, String autor, boolean disponivel, List<Aluguel> alugueis) {
        this.id = id;
        this.nome = nome;
        this.autor = autor;
        this.disponivel = disponivel;
        this.alugueis = alugueis;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String autor;

    private boolean disponivel = true;

    @OneToMany(mappedBy = "livro")
    private List<Aluguel> alugueis;
}
