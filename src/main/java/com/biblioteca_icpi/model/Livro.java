package com.biblioteca_icpi.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "TB_LIVRO")
public class Livro {

    public Livro() {
    }

    public Livro(Long id, String nome, String autor, DisponibilidadeLivro disponivel, List<Aluguel> alugueis) {
        this.id = id;
        this.nome = nome;
        this.autor = autor;
        this.disponivel = disponivel;
        this.alugueis = alugueis;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "autor")
    private String autor;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "genero")
    private String genero;

    @Enumerated(EnumType.STRING)
    @Column(name = "disponivel", nullable = false)
    private DisponibilidadeLivro disponivel;

    @OneToMany(mappedBy = "livro")
    private List<Aluguel> alugueis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public DisponibilidadeLivro getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(DisponibilidadeLivro disponivel) {
        this.disponivel = disponivel;
    }

    public List<Aluguel> getAlugueis() {
        return alugueis;
    }

    public void setAlugueis(List<Aluguel> alugueis) {
        this.alugueis = alugueis;
    }
}
