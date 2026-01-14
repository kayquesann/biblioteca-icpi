package com.biblioteca_icpi.dto;

import com.biblioteca_icpi.model.DisponibilidadeLivro;

public class LivroResponseDTO {

    private Long id;
    private String nome;
    private String autor;
    private String descricao;
    private String genero;
    private DisponibilidadeLivro status;

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

    public DisponibilidadeLivro getStatus() {
        return status;
    }

    public void setStatus(DisponibilidadeLivro status) {
        this.status = status;
    }
}
