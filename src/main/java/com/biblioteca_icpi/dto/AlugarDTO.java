package com.biblioteca_icpi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AlugarDTO {

    @NotNull(message = "O ID do livro é obrigatório.")
    private Long idLivro;

    @Email
    @NotBlank(message = "O email do usuário é obrigatório.")
    private String email;

    public Long getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(Long idLivro) {
        this.idLivro = idLivro;
    }

    public @NotBlank(message = "O email do usuário é obrigatório.") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "O email do usuário é obrigatório.") String email) {
        this.email = email;
    }
}
