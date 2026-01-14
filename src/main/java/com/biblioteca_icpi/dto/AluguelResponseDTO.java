package com.biblioteca_icpi.dto;

import com.biblioteca_icpi.model.StatusAluguelEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AluguelResponseDTO {

    private Long id;

    private String usuario;

    private String livro;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", locale = "pt-BR")
    private LocalDateTime dataInicio;

    @JsonFormat(pattern = "dd/MM/yyyy", locale = "pt-BR")
    private LocalDate prazoDevolucao;

    private StatusAluguelEnum statusAluguel;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", locale = "pt-BR")
    private LocalDateTime encerradoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getPrazoDevolucao() {
        return prazoDevolucao;
    }

    public void setPrazoDevolucao(LocalDate prazoDevolucao) {
        this.prazoDevolucao = prazoDevolucao;
    }

    public StatusAluguelEnum getStatusAluguel() {
        return statusAluguel;
    }

    public void setStatusAluguel(StatusAluguelEnum statusAluguel) {
        this.statusAluguel = statusAluguel;
    }

    public LocalDateTime getEncerradoEm() {
        return encerradoEm;
    }

    public void setEncerradoEm(LocalDateTime encerradoEm) {
        this.encerradoEm = encerradoEm;
    }
}
