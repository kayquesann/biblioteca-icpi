package com.biblioteca_icpi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class ResponseUsuarioDTO {

    @NotBlank(message = "O usuário é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email
    private String email;

    @NotEmpty
    private List<String> roles;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
