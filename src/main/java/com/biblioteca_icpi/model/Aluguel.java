package com.biblioteca_icpi.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "TB_ALUGUEL")
public class Aluguel {

    public Aluguel() {
    }

    public Aluguel(Long id, Usuario usuario, Livro livro, LocalDate dataInicio, LocalDate dataDevolucao) {
        this.id = id;
        this.usuario = usuario;
        this.livro = livro;
        this.dataInicio = dataInicio;
        this.dataDevolucao = dataDevolucao;
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

    private LocalDate dataInicio;

    private LocalDate dataDevolucao;

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
