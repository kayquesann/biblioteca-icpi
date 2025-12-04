package com.biblioteca_icpi.controller;

import com.biblioteca_icpi.dto.EditarUsuarioDTO;
import com.biblioteca_icpi.dto.ResponseUsuarioDTO;
import com.biblioteca_icpi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUsuarioDTO> editarUsuario (@PathVariable Long id, @Valid @RequestBody EditarUsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.editarUsuario(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirUsuario (@PathVariable Long id) {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseUsuarioDTO> consultarUsuario (@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioNoBanco(id));
    }

    @GetMapping
    public ResponseEntity<List<ResponseUsuarioDTO>> consultarUsuarios () {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

}
