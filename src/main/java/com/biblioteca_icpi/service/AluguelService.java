package com.biblioteca_icpi.service;

import com.biblioteca_icpi.dto.AlugarDTO;
import com.biblioteca_icpi.dto.AluguelDTO;
import com.biblioteca_icpi.exception.livro.LivroNaoEncontradoException;
import com.biblioteca_icpi.exception.usuario.UsuarioNaoEncontradoException;
import com.biblioteca_icpi.model.Aluguel;
import com.biblioteca_icpi.model.Livro;
import com.biblioteca_icpi.model.Usuario;
import com.biblioteca_icpi.repository.AluguelRepository;
import com.biblioteca_icpi.repository.LivroRepository;
import com.biblioteca_icpi.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AluguelService {

    private final AluguelRepository aluguelRepository;
    private final LivroRepository livroRepository;
    private final LivroService livroService;
    private final UsuarioRepository usuarioRepository;

    public AluguelService(AluguelRepository aluguelRepository, LivroRepository livroRepository, LivroService livroService, UsuarioRepository usuarioRepository) {
        this.aluguelRepository = aluguelRepository;
        this.livroRepository = livroRepository;
        this.livroService = livroService;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public AluguelDTO alugarLivro (AlugarDTO alugarDTO) {
        Livro livro = livroRepository.findById(alugarDTO.getIdLivro()).orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontardo"));
        Usuario usuario = usuarioRepository.findByEmail(alugarDTO.getEmail()).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado"));
        if (livro.isDisponivel()) {
            Aluguel aluguel = new Aluguel();
            aluguel.setUsuario(usuario);
            aluguel.setLivro(livro);
            aluguel.setDataInicio(LocalDate.now());
            aluguel.setDataDevolucao(LocalDate.now().plusWeeks(2));
            livroService.marcarComoAlugado(livro);
            aluguelRepository.save(aluguel);
            return convertToAluguelDTO(aluguel);
        }
        else {
            throw new IllegalStateException("O livro já está alugado!");
        }
    }

    @Transactional
    public AluguelDTO devolverLivro (Long idAluguel) {
        Aluguel aluguel = aluguelRepository.findById(idAluguel).orElseThrow(() -> new IllegalStateException("Aluguel não encontrado"));
        Livro livro = aluguel.getLivro();
        livroService.marcarComoDisponivel(livro);
        aluguel.setStatus("DEVOLVIDO");
        aluguelRepository.save(aluguel);
        return convertToAluguelDTO(aluguel);

    }

    public AluguelDTO consultarAluguelEspecifico (Long idAluguel) {
        Aluguel aluguel = aluguelRepository.findById(idAluguel).orElseThrow(() -> new IllegalStateException("Aluguel não encontrado"));
        return convertToAluguelDTO(aluguel);
    }

    public List<AluguelDTO> consultarAlugueis () {
        List<Aluguel> alugueis = aluguelRepository.findAll();
        return alugueis.
                stream()
                .map(this::convertToAluguelDTO)
                .toList();
    }

    public AluguelDTO convertToAluguelDTO (Aluguel aluguel) {
        AluguelDTO aluguelDTO = new AluguelDTO();
        aluguelDTO.setLivro(aluguel.getLivro().getNome());
        aluguelDTO.setUsuario(aluguel.getUsuario().getNome());
        aluguelDTO.setStatus(aluguel.getStatus());
        aluguelDTO.setDataInicio(aluguel.getDataInicio());
        aluguelDTO.setDataDevolucao(aluguel.getDataDevolucao());
        return aluguelDTO;
    }


}
