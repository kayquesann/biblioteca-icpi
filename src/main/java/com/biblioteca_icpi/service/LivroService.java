package com.biblioteca_icpi.service;

import com.biblioteca_icpi.exception.livro.LivroJaExistenteException;
import com.biblioteca_icpi.exception.livro.LivroNaoEncontradoException;
import com.biblioteca_icpi.model.Livro;
import com.biblioteca_icpi.repository.LivroRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro buscarLivroNoBancoDeDados (Long id) {
        return livroRepository.findById(id).orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado!"));

    }

    public Livro cadastrarLivro (Livro livro) {
        Optional<Livro> possivelLivro = livroRepository.findById(livro.getId());
        if (possivelLivro.isPresent()) {
            throw new LivroJaExistenteException("Livro já cadastrado!");
        } else {
            return livroRepository.save(livro);
        }
    }

    public Livro editarLivro (Long id, Livro livro) {
        Livro livroEncontrado = buscarLivroNoBancoDeDados(id);
        livroEncontrado.setAutor(livro.getAutor());
        livroEncontrado.setNome(livro.getNome());
        return livroRepository.save(livroEncontrado);
    }

    public void excluirLivro (Long id) {
        Livro livroEncontrado = buscarLivroNoBancoDeDados(id);
        livroRepository.delete(livroEncontrado);
    }

    public List<Livro> listarLivros () {
        return livroRepository.findAll();
    }

    public Livro buscarLivroEspecifico (Long id) {
        return buscarLivroNoBancoDeDados(id);
    }

    public void marcarComoAlugado (Livro livro) {
        if (livro.isDisponivel()) {
            livro.setDisponivel(false);
            livroRepository.save(livro);
        }
        else {
            throw new IllegalStateException("Este livro já está alugado!");
        }
    }

    public void marcarcomoDisponivel (Livro livro) {
        if (livro.isDisponivel()) {
            throw  new IllegalStateException("Este livro já está disponível!");
        }
        else {
            livro.setDisponivel(true);
            livroRepository.save(livro);
        }
    }
}
