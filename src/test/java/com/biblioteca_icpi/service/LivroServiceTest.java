package com.biblioteca_icpi.service;

import com.biblioteca_icpi.dto.CadastrarLivroDTO;
import com.biblioteca_icpi.dto.CadastrarUsuarioDTO;
import com.biblioteca_icpi.dto.LivroResponseDTO;
import com.biblioteca_icpi.exception.livro.LivroJaExistenteException;
import com.biblioteca_icpi.exception.livro.LivroNaoEncontradoException;
import com.biblioteca_icpi.model.DisponibilidadeLivro;
import com.biblioteca_icpi.model.Livro;
import com.biblioteca_icpi.repository.AluguelRepository;
import com.biblioteca_icpi.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private AluguelRepository aluguelRepository;

    @InjectMocks
    @Spy
    private LivroService livroService;

    @Captor
    private ArgumentCaptor<Livro> captor;



    @Test
    void deveBuscarLivroNoBanco () {
        Long idLivro = 1L;
        Livro livro = new Livro();

        given(livroRepository.findById(idLivro))
                .willReturn(Optional.of(livro));

        LivroResponseDTO response = livroService.buscarLivroNoBancoDeDados(idLivro);

        assertNotNull(response);
        then(livroRepository).should().findById(idLivro);
    }

    @Test
    void naoDeveEncontrarLivroNoBanco () {
        Long idLivro = 1L;

        given(livroRepository.findById(idLivro))
                .willReturn(Optional.empty());

        assertThrows(LivroNaoEncontradoException.class, () -> livroService.buscarLivroNoBancoDeDados(idLivro));
        then(livroRepository).should().findById(idLivro);
    }

    @Test
    void deveCadastrarLivro () {
        CadastrarLivroDTO dto = new CadastrarLivroDTO();
        dto.setNome("O Pequeno Principe");
        dto.setAutor("Autor");
        dto.setDescricao("Um livro legal");
        dto.setGenero("Ficção");


        given(livroRepository.findByNome(dto.getNome()))
                .willReturn(Optional.empty());

        LivroResponseDTO response = livroService.cadastrarLivro(dto);

        then(livroRepository).should().findByNome(dto.getNome());
        then(livroRepository).should().save(any(Livro.class));
        assertNotNull(response);
        assertEquals(dto.getNome(), response.getNome());
        assertEquals(dto.getAutor(), response.getAutor());
        assertEquals(dto.getDescricao(), response.getDescricao());
        assertEquals(dto.getGenero(), response.getGenero());
    }

    @Test
    void naoDevePermitirCadastrarLivro () {
        CadastrarLivroDTO dto = new CadastrarLivroDTO();
        dto.setNome("O Pequeno Principe");
        Livro livroEncontrado = new Livro();
        livroEncontrado.setNome("O Pequeno Principe");

        given(livroRepository.findByNome(dto.getNome()))
                .willReturn(Optional.of(livroEncontrado));

        assertThrows(LivroJaExistenteException.class, () -> livroService.cadastrarLivro(dto));
        then(livroRepository).should().findByNome(dto.getNome());
    }

    @Test
    void devePermitirEditarLivro () {
        Long idLivro = 1L;
        CadastrarLivroDTO dto = new CadastrarLivroDTO();
        dto.setNome("O Pequeno Principe");
        dto.setGenero("Ficção");
        dto.setDescricao("Um livro legal");
        dto.setAutor("Autor");

        Livro livroEncontrado = new Livro();

        given(livroRepository.findById(idLivro))
                .willReturn(Optional.of(livroEncontrado));

        LivroResponseDTO response = livroService.editarLivro(idLivro, dto);

        then(livroRepository).should().findById(idLivro);
        then(livroRepository).should().save(livroEncontrado);
        assertNotNull(response);
        assertEquals(dto.getNome(), response.getNome());
        assertEquals(dto.getGenero(), response.getGenero());
        assertEquals(dto.getDescricao(), response.getDescricao());
        assertEquals(dto.getAutor(), response.getAutor());
    }

    @Test
    void naoDevePermitirEditarLivro() {
        Long idLivro = 1L;
        CadastrarLivroDTO dto = new CadastrarLivroDTO();
        dto.setNome("O Pequeno Principe");
        dto.setGenero("Ficção");
        dto.setDescricao("Um livro legal");
        dto.setAutor("Autor");

        given(livroRepository.findById(idLivro))
                .willReturn(Optional.empty());

        assertThrows(LivroNaoEncontradoException.class, () -> livroService.editarLivro(idLivro, dto));
    }

    @Test
    void devePermitirExcluirLivro () {
        Long idLivro = 1L;

        Livro livroEncontrado = new Livro();


        given(livroRepository.findById(idLivro))
                .willReturn(Optional.of(livroEncontrado));

        given(aluguelRepository.existsByLivro(livroEncontrado))
                .willReturn(false);

        livroService.excluirLivro(idLivro);

        then(livroRepository).should().findById(idLivro);

        then(livroRepository).should().delete(livroEncontrado);
    }

    @Test
    void naoDeveAcharOLivroParaPermitirExclusao () {
        Long idLivro = 1L;

        given(livroRepository.findById(idLivro))
                .willReturn(Optional.empty());

        assertThrows(LivroNaoEncontradoException.class, () ->livroService.excluirLivro(idLivro));
    }

    @Test
    void naoDevePermitirExclusãoDeLivroPorTerAluguelAssociado () {
        Long idLivro = 1L;

        Livro livroEncontrado = new Livro();

        given(livroRepository.findById(idLivro))
                .willReturn(Optional.of(livroEncontrado));

        given(aluguelRepository.existsByLivro(livroEncontrado))
                .willReturn(true);

        assertThrows(IllegalStateException.class, () -> livroService.excluirLivro(idLivro));
    }

    @Test
    void deveListarLivros () {
        List<Livro> livrosEncontrados = new ArrayList<>();

        Livro primeiroLivro = new Livro();
        primeiroLivro.setNome("O Pequeno Principe");

        Livro segundoLivro = new Livro();
        segundoLivro.setNome("A Cabana");

        livrosEncontrados.add(primeiroLivro);
        livrosEncontrados.add(segundoLivro);

        given(livroRepository.findAll())
                .willReturn(livrosEncontrados);

        List<LivroResponseDTO> response = livroService.listarLivros();


        then(livroRepository).should().findAll();
        assertEquals(2, response.size());
        assertEquals("O Pequeno Principe", response.get(0).getNome());
        assertEquals("A Cabana", response.get(1).getNome());
    }

    @Test
    void deveMarcarLivroComoAlugado () {
        Livro livro = new Livro();
        livro.setDisponivel(DisponibilidadeLivro.DISPONIVEL);

        livroService.marcarComoAlugado(livro);

        then(livroRepository).should().save(captor.capture());

        Livro livroSalvo = captor.getValue();

        assertEquals(DisponibilidadeLivro.ALUGADO, livroSalvo.getDisponivel());

    }

    @Test
    void naoDevePermitirMarcarLivroComoAlugado () {
        Livro livro = new Livro();
        livro.setDisponivel(DisponibilidadeLivro.ALUGADO);

        assertThrows(IllegalStateException.class, () -> livroService.marcarComoAlugado(livro));
    }

    @Test
    void deveMarcarLivroComoDisponivel () {
        Livro livro = new Livro();
        livro.setDisponivel(DisponibilidadeLivro.ALUGADO);

        livroService.marcarComoDisponivel(livro);

        then(livroRepository).should().save(captor.capture());

        Livro livroSalvo = captor.getValue();

        assertEquals(DisponibilidadeLivro.DISPONIVEL, livroSalvo.getDisponivel());

    }

    @Test
    void naoDevePermitirLivroMarcarComoDisponivel () {
        Livro livro = new Livro();
        livro.setDisponivel(DisponibilidadeLivro.DISPONIVEL);

        assertThrows(IllegalStateException.class, () -> livroService.marcarComoDisponivel(livro));
    }

    @Test
    void deveConverterLivroParaLivroResponseDTO () {

        Livro livro = new Livro();
        livro.setId(1L);
        livro.setNome("O Pequeno Principe");
        livro.setAutor("Autor");
        livro.setGenero("Ficção");
        livro.setDescricao("Um livro legal");
        livro.setDisponivel(DisponibilidadeLivro.DISPONIVEL);

        LivroResponseDTO response = livroService.convertToLivroResponseDTO(livro);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("O Pequeno Principe", response.getNome());
        assertEquals("Autor", response.getAutor());
        assertEquals("Ficção", response.getGenero());
        assertEquals("Um livro legal", response.getDescricao());
        assertEquals(DisponibilidadeLivro.DISPONIVEL, response.getStatus());
    }


}