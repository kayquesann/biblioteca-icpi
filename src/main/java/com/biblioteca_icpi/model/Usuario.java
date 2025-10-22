package com.biblioteca_icpi.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "TB_USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    @OneToMany(mappedBy = "usuario")
    private List<Aluguel> alugueis;
}
