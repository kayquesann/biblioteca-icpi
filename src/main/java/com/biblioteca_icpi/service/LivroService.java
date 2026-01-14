package com.biblioteca_icpi.service;

import com.biblioteca_icpi.dto.CadastrarLivroDTO;
import com.biblioteca_icpi.dto.LivroResponseDTO;
import com.biblioteca_icpi.exception.livro.LivroJaExistenteException;
import com.biblioteca_icpi.exception.livro.LivroNaoEncontradoException;
import com.biblioteca_icpi.model.DisponibilidadeLivro;
import com.biblioteca_icpi.model.Livro;
import com.biblioteca_icpi.repository.AluguelRepository;
import com.biblioteca_icpi.repository.LivroRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private  final AluguelRepository aluguelRepository;

    public LivroService(LivroRepository livroRepository, AluguelRepository aluguelRepository) {
        this.livroRepository = livroRepository;
        this.aluguelRepository = aluguelRepository;
    }

    public LivroResponseDTO buscarLivroNoBancoDeDados (Long idLivro) {
        Livro livro = livroRepository.findById(idLivro).orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado!"));
        return convertToLivroResponseDTO(livro);
    }

    @Transactional
    public LivroResponseDTO cadastrarLivro (CadastrarLivroDTO dto) {
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
            livro.setDisponivel(DisponibilidadeLivro.DISPONIVEL);
            livroRepository.save(livro);
            return convertToLivroResponseDTO(livro);
        }
    }

    @Transactional
    public LivroResponseDTO editarLivro (Long idLivro, CadastrarLivroDTO dto) {
        Livro livroEncontrado = livroRepository.findById(idLivro).orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado!"));
        livroEncontrado.setAutor(dto.getAutor());
        livroEncontrado.setNome(dto.getNome());
        livroEncontrado.setDescricao(dto.getDescricao());
        livroEncontrado.setGenero(dto.getGenero());
        livroRepository.save(livroEncontrado);
        return convertToLivroResponseDTO(livroEncontrado);
    }

    public void excluirLivro (Long idLivro) {
        Livro livroEncontrado = livroRepository.findById(idLivro).orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado!"));
        if (aluguelRepository.existsByLivro(livroEncontrado)) {
            throw new IllegalStateException("O livro não pode ser deletado pois possui histórico de alugueis");
        }
        livroRepository.delete(livroEncontrado);
    }

    public List<LivroResponseDTO> listarLivros () {
        List<Livro> livros = livroRepository.findAll();
        return livros.stream().map(this::convertToLivroResponseDTO).toList();
    }


    public void marcarComoAlugado (Livro livro) {
        if (livro.getDisponivel() == DisponibilidadeLivro.DISPONIVEL) {
            livro.setDisponivel(DisponibilidadeLivro.ALUGADO);
            livroRepository.save(livro);
        }
        else {
            throw new IllegalStateException("Este livro já está alugado!");
        }
    }

    public void marcarComoDisponivel (Livro livro) {
        if (livro.getDisponivel() == DisponibilidadeLivro.DISPONIVEL) {
            throw  new IllegalStateException("Este livro já está disponível!");
        }
        else {
            livro.setDisponivel(DisponibilidadeLivro.DISPONIVEL);
            livroRepository.save(livro);
        }
    }

    public LivroResponseDTO convertToLivroResponseDTO (Livro livro) {
        LivroResponseDTO livroResponseDTO = new LivroResponseDTO();
        livroResponseDTO.setId(livro.getId());
        livroResponseDTO.setNome(livro.getNome());
        livroResponseDTO.setAutor(livro.getAutor());
        livroResponseDTO.setDescricao(livro.getDescricao());
        livroResponseDTO.setGenero(livro.getGenero());
        livroResponseDTO.setStatus(livro.getDisponivel());
        return livroResponseDTO;
    }
}
