package com.biblioteca_icpi.service;

import com.biblioteca_icpi.dto.LivroDTO;
import com.biblioteca_icpi.exception.livro.LivroJaExistenteException;
import com.biblioteca_icpi.exception.livro.LivroNaoEncontradoException;
import com.biblioteca_icpi.model.Livro;
import com.biblioteca_icpi.repository.LivroRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public LivroDTO buscarLivroNoBancoDeDados (Long idLivro) {
        Livro livro = livroRepository.findById(idLivro).orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado!"));
        return convertToLivroDTO(livro);
    }

    @Transactional
    public LivroDTO cadastrarLivro (LivroDTO dto) {
        Optional<Livro> possivelLivro = livroRepository.findByNome(dto.getNome());
        if (possivelLivro.isPresent()) {
            throw new LivroJaExistenteException("Livro já cadastrado!");
        }
        else{
            Livro livro = new Livro();
            livro.setNome(dto.getNome());
            livro.setAutor(dto.getAutor());
            livro.setDescricao(dto.getDescricao());
            livro.setGenero(dto.getGenero());
            livroRepository.save(livro);
            return convertToLivroDTO(livro);
        }
    }

    @Transactional
    public LivroDTO editarLivro (Long idLivro, LivroDTO dto) {
        Livro livroEncontrado = livroRepository.findById(idLivro).orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado!"));
        livroEncontrado.setAutor(dto.getAutor());
        livroEncontrado.setNome(dto.getNome());
        livroEncontrado.setDescricao(dto.getDescricao());
        livroEncontrado.setGenero(dto.getGenero());
        livroRepository.save(livroEncontrado);
        return convertToLivroDTO(livroEncontrado);
    }

    public void excluirLivro (Long idLivro) {
        Livro livroEncontrado = livroRepository.findById(idLivro).orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado!"));
        livroRepository.delete(livroEncontrado);
    }

    public List<LivroDTO> listarLivros () {
        List<Livro> livros = livroRepository.findAll();
        return livros.stream().map(this::convertToLivroDTO).toList();
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

    public void marcarComoDisponivel (Livro livro) {
        if (livro.isDisponivel()) {
            throw  new IllegalStateException("Este livro já está disponível!");
        }
        else {
            livro.setDisponivel(true);
            livroRepository.save(livro);
        }
    }

    public LivroDTO convertToLivroDTO (Livro livro) {
        LivroDTO livroDTO = new LivroDTO();
        livroDTO.setNome(livro.getNome());
        livroDTO.setAutor(livro.getAutor());
        livroDTO.setDescricao(livro.getDescricao());
        livroDTO.setGenero(livro.getGenero());
        return livroDTO;
    }
}
