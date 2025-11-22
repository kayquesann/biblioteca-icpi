package com.biblioteca_icpi.controller;

import com.biblioteca_icpi.dto.CadastrarUsuarioDTO;
import com.biblioteca_icpi.dto.EditarUsuarioDTO;
import com.biblioteca_icpi.model.Aluguel;
import com.biblioteca_icpi.model.Usuario;
import com.biblioteca_icpi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Usuario cadastrar (@Valid @RequestBody CadastrarUsuarioDTO dto) {
        return usuarioService.criarUsuario(dto);
    }

    @PutMapping("/{id}")
    public Usuario editarUsuario (@Valid @PathVariable Long id, @RequestBody EditarUsuarioDTO dto) {
        return usuarioService.editarUsuario(id, dto);
    }

    @DeleteMapping("/{id}")
    public void excluirUsuario (@PathVariable Long id) {
        usuarioService.excluirUsuario(id);
    }


    @GetMapping("/{id}")
    public Usuario consultarUsuario (@PathVariable Long idUsuario) {
        return usuarioService.buscarUsuarioNoBanco(idUsuario);
    }

    @GetMapping
    public List<Usuario> consultarUsuarios () {
        return usuarioService.listarUsuarios();
    }

}
