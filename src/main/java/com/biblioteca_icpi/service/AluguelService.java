package com.biblioteca_icpi.service;

import com.biblioteca_icpi.dto.AlugarDTO;
import com.biblioteca_icpi.dto.AluguelResponseDTO;
import com.biblioteca_icpi.exception.livro.LivroNaoEncontradoException;
import com.biblioteca_icpi.exception.usuario.UsuarioNaoEncontradoException;
import com.biblioteca_icpi.model.*;
import com.biblioteca_icpi.repository.AluguelRepository;
import com.biblioteca_icpi.repository.LivroRepository;
import com.biblioteca_icpi.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        public AluguelResponseDTO alugarLivro (AlugarDTO alugarDTO) {
            Livro livro = livroRepository.findById(alugarDTO.getIdLivro()).orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado"));
            Usuario usuario = usuarioRepository.findByEmail(alugarDTO.getEmail()).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        if (livro.getDisponivel() == DisponibilidadeLivro.DISPONIVEL) {
            Aluguel aluguel = new Aluguel();
            aluguel.setUsuario(usuario);
            aluguel.setLivro(livro);
            aluguel.setDataInicio(LocalDateTime.now());
            aluguel.setPrazoDevolucao(LocalDate.now().plusWeeks(2));
            livroService.marcarComoAlugado(livro);
            aluguel.setStatus(StatusAluguelEnum.ATIVO);
            aluguelRepository.save(aluguel);
            return convertToAluguelResponseDTO(aluguel);
        }
        else {
            throw new IllegalStateException("O livro já está alugado!");
        }
    }

    @Transactional
    public AluguelResponseDTO devolverLivro (Long id) {
        Aluguel aluguel = aluguelRepository.findById(id).orElseThrow(() -> new IllegalStateException("Aluguel não encontrado"));
        Livro livro = aluguel.getLivro();
        livroService.marcarComoDisponivel(livro);
        aluguel.setStatus(StatusAluguelEnum.ENCERRADO);
        aluguel.setEncerradoEm(LocalDateTime.now());
        aluguelRepository.save(aluguel);
        return convertToAluguelResponseDTO(aluguel);

    }

    private Usuario obterUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }

    public AluguelResponseDTO consultarAluguelEspecifico (Long idAluguel) throws AccessDeniedException {

        Usuario usuarioLogado = obterUsuarioLogado();
        Aluguel aluguel = aluguelRepository.findById(idAluguel).orElseThrow(() -> new IllegalStateException("Aluguel não encontrado"));

        if (usuarioLogado.getRole() == UserRole.ADMIN) {
            return convertToAluguelResponseDTO(aluguel);
        }

        if (!aluguel.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem permissão para acessar este aluguel");
        }
        return convertToAluguelResponseDTO(aluguel);
    }

    public List<AluguelResponseDTO> consultarAlugueis () {
        Usuario usuarioLogado = obterUsuarioLogado();
        if (usuarioLogado.getRole() == UserRole.ADMIN) {
            return aluguelRepository.findAll()
                    .stream()
                    .map(this::convertToAluguelResponseDTO)
                    .toList();
        }
        return aluguelRepository.findByUsuario(usuarioLogado)
                .stream()
                .map(this::convertToAluguelResponseDTO)
                .toList();
    }



    public AluguelResponseDTO convertToAluguelResponseDTO (Aluguel aluguel) {
        AluguelResponseDTO aluguelEncerradoDTO = new AluguelResponseDTO();
        aluguelEncerradoDTO.setId(aluguel.getId());
        aluguelEncerradoDTO.setLivro(aluguel.getLivro().getNome());
        aluguelEncerradoDTO.setUsuario(aluguel.getUsuario().getNome());
        aluguelEncerradoDTO.setStatusAluguel(aluguel.getStatus());
        aluguelEncerradoDTO.setDataInicio(aluguel.getDataInicio());
        aluguelEncerradoDTO.setPrazoDevolucao(aluguel.getPrazoDevolucao());
        aluguelEncerradoDTO.setEncerradoEm(aluguel.getEncerradoEm());
        return aluguelEncerradoDTO;
    }


}
