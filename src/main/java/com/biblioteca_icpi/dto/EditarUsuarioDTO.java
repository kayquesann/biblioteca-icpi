package com.biblioteca_icpi.dto;

import jakarta.validation.constraints.NotBlank;

public class EditarUsuarioDTO {

    @NotBlank(message = "O nome do usuário é obrigatório.")
    private String nome;

    @NotBlank(message = "A senha é obrigatória.")
    private String senha;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
