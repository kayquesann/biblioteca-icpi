package com.biblioteca_icpi.service;

import com.biblioteca_icpi.model.Aluguel;
import com.biblioteca_icpi.model.Livro;
import com.biblioteca_icpi.model.Usuario;
import com.biblioteca_icpi.repository.AluguelRepository;
import com.biblioteca_icpi.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AluguelService {

    private final AluguelRepository aluguelRepository;
    private final LivroRepository livroRepository;
    private final LivroService livroService;
    private final UsuarioService usuarioService;

    public AluguelService(AluguelRepository aluguelRepository, LivroRepository livroRepository, LivroService livroService, UsuarioService usuarioService) {
        this.aluguelRepository = aluguelRepository;
        this.livroRepository = livroRepository;
        this.livroService = livroService;
        this.usuarioService = usuarioService;
    }

    public Aluguel alugarLivro (Long idUsuario, Long idLivro) {
        Livro livro = livroService.buscarLivroNoBancoDeDados(idLivro);
        Usuario usuario = usuarioService.buscarUsuarioNoBanco(idUsuario);
        if (livro.isDisponivel()) {
            Aluguel aluguel = new Aluguel();
            aluguel.setUsuario(usuario);
            aluguel.setLivro(livro);
            aluguel.setDataInicio(LocalDate.now());
            aluguel.setDataDevolucao(LocalDate.now().plusWeeks(2));
            livroService.marcarComoAlugado(livro);
            Aluguel aluguelSalvo = aluguelRepository.save(aluguel);
            return aluguelSalvo;
        }
        else {
            throw new IllegalStateException("O livro já está alugado!");
        }
    }

    public Aluguel devolverLivro (Long idAluguel) {
        Aluguel aluguel = aluguelRepository.findById(idAluguel).orElseThrow(() -> new IllegalStateException("Aluguel não encontrado"));
        Livro livro = aluguel.getLivro();
        livroService.marcarcomoDisponivel(livro);
        aluguel.setStatus("DEVOLVIDO");
        Aluguel aluguelSalvo = aluguelRepository.save(aluguel);
        return aluguelSalvo;
    }

    public Aluguel consultarAluguelEspecifico (Long idAluguel) {
        return aluguelRepository.findById(idAluguel).orElseThrow(() -> new IllegalStateException("Aluguel não encontrado"));
    }

    public List<Aluguel> consultarAlugueis () {
        List<Aluguel> alugueis = aluguelRepository.findAll();
        return alugueis;
    }


}
