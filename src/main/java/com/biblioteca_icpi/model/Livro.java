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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getNome() {
        return nome;
    }

    public void setNome(@NotBlank String nome) {
        this.nome = nome;
    }

    public @NotBlank String getAutor() {
        return autor;
    }

    public void setAutor(@NotBlank String autor) {
        this.autor = autor;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public List<Aluguel> getAlugueis() {
        return alugueis;
    }

    public void setAlugueis(List<Aluguel> alugueis) {
        this.alugueis = alugueis;
    }
}
