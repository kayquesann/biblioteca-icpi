package com.biblioteca_icpi.controller;

import com.biblioteca_icpi.model.Usuario;
import com.biblioteca_icpi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Usuario cadastrar (@Valid @RequestBody Usuario usuario) {
        return usuarioService.criarUsuario(usuario);
    }

    @PutMapping("/{id}")
    public Usuario editarUsuario (@Valid @PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.editarUsuario(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void excluirUsuario (@PathVariable Long id) {
        usuarioService.excluirUsuario(id);
    }


    @GetMapping("/{id}")
    public Usuario lerUsuario (@PathVariable Long id) {
        return usuarioService.lerUsuario(id);
    }


}
