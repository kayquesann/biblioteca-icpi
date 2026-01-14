package com.biblioteca_icpi.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_ALUGUEL")
public class Aluguel {

    public Aluguel() {
    }

    public Aluguel(Long id, Usuario usuario, Livro livro, LocalDateTime dataInicio, LocalDate prazoDevolucao) {
        this.id = id;
        this.usuario = usuario;
        this.livro = livro;
        this.dataInicio = dataInicio;
        this.prazoDevolucao = prazoDevolucao;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "livro_id")
    private Livro livro;

    @Column(name = "status")
    private StatusAluguelEnum status;

    @Column(name = "dataInicio")
    private LocalDateTime dataInicio;

    @Column(name = "prazoDevolucao")
    private LocalDate prazoDevolucao;

    @Column(name = "encerradoEm")
    private LocalDateTime encerradoEm;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public StatusAluguelEnum getStatus() {
        return status;
    }

    public void setStatus(StatusAluguelEnum status) {
        this.status = status;
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

    public LocalDateTime getEncerradoEm() {
        return encerradoEm;
    }

    public void setEncerradoEm(LocalDateTime encerradoEm) {
        this.encerradoEm = encerradoEm;
    }
}
