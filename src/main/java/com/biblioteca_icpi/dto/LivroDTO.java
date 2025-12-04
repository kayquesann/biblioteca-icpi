package com.biblioteca_icpi.dto;

import jakarta.validation.constraints.NotBlank;

public class LivroDTO {

    @NotBlank(message = "O nome do livro é obrigatório.")
    private String nome;

    @NotBlank(message = "O autor é obrigatório.")
    private String autor;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    @NotBlank(message = "O gênero é obrigatório")
    private String genero;

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
}
