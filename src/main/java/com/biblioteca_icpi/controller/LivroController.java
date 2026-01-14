package com.biblioteca_icpi.controller;

import com.biblioteca_icpi.dto.CadastrarLivroDTO;
import com.biblioteca_icpi.dto.LivroResponseDTO;
import com.biblioteca_icpi.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarLivro (@PathVariable Long id) {
        return ResponseEntity.ok(livroService.buscarLivroNoBancoDeDados(id));
    }

    @PostMapping
    public ResponseEntity<LivroResponseDTO> cadastrarLivro (@Valid @RequestBody CadastrarLivroDTO dto) {
        return ResponseEntity.ok(livroService.cadastrarLivro(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> editarLivro (@PathVariable Long id, @Valid @RequestBody CadastrarLivroDTO dto) {
        return ResponseEntity.ok(livroService.editarLivro(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro (@PathVariable Long id) {
        livroService.excluirLivro(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listarLivros () {
        return ResponseEntity.ok(livroService.listarLivros());
    }
}
