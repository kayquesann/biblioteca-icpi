package com.biblioteca_icpi.controller;

import com.biblioteca_icpi.dto.CadastrarUsuarioDTO;
import com.biblioteca_icpi.dto.ResponseUsuarioDTO;
import com.biblioteca_icpi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;


    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ResponseUsuarioDTO> registrar (@Valid @RequestBody CadastrarUsuarioDTO dto) {
        ResponseUsuarioDTO usuarioCriado = usuarioService.criarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/cadastrar")
    public ResponseEntity<ResponseUsuarioDTO> registrarAdmin (@Valid @RequestBody CadastrarUsuarioDTO dto) {
        ResponseUsuarioDTO usuarioCriado = usuarioService.criarUsuarioAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }
}
