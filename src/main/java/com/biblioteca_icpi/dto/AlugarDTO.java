package com.biblioteca_icpi.dto;

import jakarta.validation.constraints.NotNull;

public class AlugarDTO {

    @NotNull(message = "O ID do livro é obrigatório.")
    private Long idLivro;

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Long idUsuario;

    public Long getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(Long idLivro) {
        this.idLivro = idLivro;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
