package com.biblioteca_icpi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AluguelDTO {

    @NotBlank(message = "o usuário é obrigatório.")
    private String usuario;

    @NotBlank(message = "o usuário é obrigatório.")
    private String livro;

    @NotBlank(message = "o usuário é obrigatório.")
    private String status;

    @NotNull(message = "data de início é obrigatória.")
    private LocalDate dataInicio;

    @NotNull(message = "data de devolução é obrigatória.")
    private LocalDate dataDevolucao;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getLivro() {
        return livro;
    }

    public void setLivro(String livro) {
        this.livro = livro;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }
}
