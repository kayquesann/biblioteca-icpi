package com.biblioteca_icpi.controller;

import com.biblioteca_icpi.dto.CadastrarLivroDTO;
import com.biblioteca_icpi.dto.EditarLivroDTO;
import com.biblioteca_icpi.model.Livro;
import com.biblioteca_icpi.service.LivroService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping("/{idLivro}")
    public Livro buscarLivro (@PathVariable Long idLivro) {
        return livroService.buscarLivroNoBancoDeDados(idLivro);
    }

    @PostMapping
    public Livro cadastrarLivro (@RequestBody CadastrarLivroDTO dto) {
        return livroService.cadastrarLivro(dto);
    }

    @PutMapping("/{idLivro}")
    public Livro editarLivro (@PathVariable Long idLivro, @RequestBody EditarLivroDTO dto) {
        return livroService.editarLivro(idLivro, dto);
    }

    @DeleteMapping("/{idLivro}")
    public void deletarLivro (@PathVariable Long idLivro) {
        livroService.excluirLivro(idLivro);
    }

    @GetMapping
    public List<Livro> listarLivros () {
        return livroService.listarLivros();
    }
}
