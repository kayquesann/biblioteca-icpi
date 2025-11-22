package com.biblioteca_icpi.service;

import com.biblioteca_icpi.dto.CadastrarLivroDTO;
import com.biblioteca_icpi.dto.EditarLivroDTO;
import com.biblioteca_icpi.exception.livro.LivroJaExistenteException;
import com.biblioteca_icpi.exception.livro.LivroNaoEncontradoException;
import com.biblioteca_icpi.exception.usuario.UsuarioJaExistenteException;
import com.biblioteca_icpi.model.Livro;
import com.biblioteca_icpi.model.Usuario;
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

    public Livro buscarLivroNoBancoDeDados (Long idLivro) {
        return livroRepository.findById(idLivro).orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado!"));

    }

    public Livro cadastrarLivro (CadastrarLivroDTO dto) {
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
            Livro livroSalvo = livroRepository.save(livro);
            return livroSalvo;
        }
    }

    public Livro editarLivro (Long idLivro, EditarLivroDTO dto) {
        Livro livroEncontrado = buscarLivroNoBancoDeDados(idLivro);
        livroEncontrado.setAutor(dto.getAutor());
        livroEncontrado.setNome(dto.getNome());
        livroEncontrado.setDescricao(dto.getDescricao());
        livroEncontrado.setGenero(dto.getGenero());
        Livro livroSalvo = livroRepository.save(livroEncontrado);
        return livroSalvo;
    }

    public void excluirLivro (Long idLivro) {
        Livro livroEncontrado = buscarLivroNoBancoDeDados(idLivro);
        livroRepository.delete(livroEncontrado);
    }

    public List<Livro> listarLivros () {
        return livroRepository.findAll();
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
