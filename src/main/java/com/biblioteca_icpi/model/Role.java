package com.biblioteca_icpi.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name = "TB_ROLE")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome; //ROLE_ADMIN, ROLE_USER

    @Override
    public String getAuthority() {
        return nome;
    }
}
