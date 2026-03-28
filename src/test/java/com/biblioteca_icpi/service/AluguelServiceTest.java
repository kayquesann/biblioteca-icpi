package com.biblioteca_icpi.service;

import com.biblioteca_icpi.dto.AlugarDTO;
import com.biblioteca_icpi.dto.AluguelResponseDTO;
import com.biblioteca_icpi.exception.livro.LivroNaoEncontradoException;
import com.biblioteca_icpi.exception.usuario.UsuarioNaoEncontradoException;
import com.biblioteca_icpi.model.*;
import com.biblioteca_icpi.repository.AluguelRepository;
import com.biblioteca_icpi.repository.LivroRepository;
import com.biblioteca_icpi.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AluguelServiceTest {

    @Spy
    @InjectMocks
    private AluguelService aluguelService;

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private LivroService livroService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Livro livro;

    @Mock
    private Usuario usuario;

    @Mock
    private Aluguel aluguel;



    @Captor
    ArgumentCaptor<Aluguel> aluguelCaptor;

    private Aluguel criarAluguel(Usuario usuario, Livro livro) {
        Aluguel aluguel = new Aluguel();
        aluguel.setUsuario(usuario);
        aluguel.setLivro(livro);
        aluguel.setStatus(StatusAluguelEnum.ATIVO);
        aluguel.setDataInicio(LocalDateTime.now());
        aluguel.setPrazoDevolucao(LocalDate.now().plusWeeks(2));
        return aluguel;
    }


    @Test
    void devoConseguirAlugarLivroComDTOCorreto() {

        // ARRANGE
        AlugarDTO alugarDTO = new AlugarDTO(1L,"kayque.teste@gmail.com");

        Livro livroCriado = new Livro();
        livroCriado.setDisponivel(DisponibilidadeLivro.DISPONIVEL);
        Usuario usuarioCriado = new Usuario();


        given(livroRepository.findById(alugarDTO.getIdLivro()))
                .willReturn(Optional.of(livroCriado));

        given(usuarioRepository.findByEmail(alugarDTO.getEmail()))
                .willReturn(Optional.of(usuarioCriado));


        // ACT
        AluguelResponseDTO response = aluguelService.alugarLivro(alugarDTO);

        // ASSERT
        then(livroService).should().marcarComoAlugado(livroCriado);
        then(aluguelRepository).should().save(aluguelCaptor.capture());

        Aluguel aluguelSalvo = aluguelCaptor.getValue();

        assertEquals(StatusAluguelEnum.ATIVO, aluguelSalvo.getStatus());
        assertEquals(usuarioCriado, aluguelSalvo.getUsuario());
        assertEquals(livroCriado, aluguelSalvo.getLivro());
        assertNotNull(aluguelSalvo.getPrazoDevolucao());

        assertNotNull(response);
    }

    @Test
    void naoDevoConseguirAlugarLivroQuandoLivroNaoExiste () {
        // ARRANGE
        AlugarDTO alugarDTO = new AlugarDTO(1L,"kayque.teste@gmail.com");

        given(livroRepository.findById(alugarDTO.getIdLivro()))
                .willReturn(Optional.empty());

        // ACT + ASSERT
        Assertions.assertThrows(LivroNaoEncontradoException.class, () -> aluguelService.alugarLivro(alugarDTO));
    }

    @Test
    void naoDevoConseguirAlugarLivroQuandoUsuarioNaoExiste () {
        // ARRANGE
        AlugarDTO alugarDTO = new AlugarDTO(1L,"kayque.teste@gmail.com");
        Livro livroCriado = new Livro();
        livroCriado.setDisponivel(DisponibilidadeLivro.DISPONIVEL);

        given(livroRepository.findById(alugarDTO.getIdLivro()))
                .willReturn(Optional.of(livroCriado));

        given(usuarioRepository.findByEmail(alugarDTO.getEmail()))
                .willReturn(Optional.empty());

        // ACT + ASSERT
        Assertions.assertThrows(UsuarioNaoEncontradoException.class, () -> aluguelService.alugarLivro(alugarDTO));
    }

    @Test
    void naoDevoConseguirAlugarLivroQuandoLivroEstaIndisponivel () {
        //ARRANGE
        AlugarDTO alugarDTO = new AlugarDTO(1L,"kayque.teste@gmail.com");

        Livro livroCriado = new Livro();
        livroCriado.setDisponivel(DisponibilidadeLivro.ALUGADO);

        Usuario usuarioCriado = new Usuario();

        given(livroRepository.findById(alugarDTO.getIdLivro()))
                .willReturn(Optional.of(livroCriado));

        given(usuarioRepository.findByEmail(alugarDTO.getEmail()))
                .willReturn(Optional.of(usuarioCriado));

        // ACT + ASSERT
        Assertions.assertThrows(IllegalStateException.class, () -> aluguelService.alugarLivro(alugarDTO));
    }

    @Test
     void devoConseguirDevolverLivro () {
        //ARRANGE
        Long id = 1L;
        Livro livroCriado = new Livro();
        livroCriado.setDisponivel(DisponibilidadeLivro.ALUGADO);

        Usuario usuarioCriado = new Usuario();

        Aluguel aluguelCriado = criarAluguel(usuarioCriado, livroCriado);


        given(aluguelRepository.findById(id)).willReturn(Optional.of(aluguelCriado));

        doNothing().when(livroService).marcarComoDisponivel(any());

        //ACT
        aluguelService.devolverLivro(id);

        //ASSERT
        assertEquals(StatusAluguelEnum.ENCERRADO, aluguelCriado.getStatus());
        assertNotNull(aluguelCriado.getEncerradoEm());

        then(livroService).should().marcarComoDisponivel(livroCriado);
        then(aluguelRepository).should().save(aluguelCriado);

    }

    @Test
    void naoDevoConseguirDevolverLivroComAluguelNaoEncontrado () {
        //ARRANGE
        Long id = 1L;

        given(aluguelRepository.findById(id))
                .willReturn(Optional.empty());

        //ACT + ASSERT
        assertThrows(IllegalStateException.class, () -> aluguelService.devolverLivro(id));
    }

    @Test
    void naoDevoConseguirDevolverLivroComAluguelEncerrado () {
        Long id = 1L;

        Aluguel aluguelCriado = new Aluguel();
        aluguelCriado.setStatus(StatusAluguelEnum.ENCERRADO);

        given(aluguelRepository.findById(id))
                .willReturn(Optional.of(aluguelCriado));

        assertThrows(IllegalStateException.class, () -> aluguelService.devolverLivro(id));
    }

    @Test
    void devoConsultarAluguelEspecificoSendoAdmin () throws AccessDeniedException {
        //ARRANGE
        Long id = 1L;

        Usuario usuarioCriado = new Usuario();
        usuarioCriado.setRole(UserRole.ADMIN);

        Livro livroCriado = new Livro();

        Aluguel aluguelCriado = criarAluguel(usuarioCriado, livroCriado);

        given(aluguelRepository.findById(id)).willReturn(Optional.of(aluguelCriado));

        doReturn(usuarioCriado).when(aluguelService).obterUsuarioLogado();

        //ACT
        AluguelResponseDTO response = aluguelService.consultarAluguelEspecifico(id);

        //ASSERT
        assertNotNull(response);
        then(aluguelRepository).should().findById(id);
    }

    @Test
    void devoConsultarAluguelEspecificoSendoMeuProprioAluguel () throws AccessDeniedException {
        Long id = 1L;

        Usuario usuarioCriado = new Usuario();
        usuarioCriado.setRole(UserRole.USER);
        usuarioCriado.setId(1L);

        Livro livroCriado = new Livro();

        Aluguel aluguelCriado = criarAluguel(usuarioCriado, livroCriado);

        given(aluguelRepository.findById(id)).willReturn(Optional.of(aluguelCriado));

        doReturn(usuarioCriado).when(aluguelService).obterUsuarioLogado();

        AluguelResponseDTO response = aluguelService.consultarAluguelEspecifico(id);

        assertNotNull(response);
        then(aluguelRepository).should().findById(id);

    }

    @Test
    void naoDevoConsultarAluguelEspecifico () {
        Long id = 1L;
        Usuario usuarioCriado = new Usuario();
        usuarioCriado.setRole(UserRole.USER);
        given(aluguelRepository.findById(id)).willReturn(Optional.empty());
        doReturn(usuarioCriado).when(aluguelService).obterUsuarioLogado();

        assertThrows(IllegalStateException.class, () -> aluguelService.consultarAluguelEspecifico(id));
        then(aluguelRepository).should().findById(id);

    }

        @Test
        void devoConsultarAlugueisSendoAdmin () {
            Usuario usuarioCriado = new Usuario();
            usuarioCriado.setRole(UserRole.ADMIN);
            doReturn(usuarioCriado).when(aluguelService).obterUsuarioLogado();

            Livro livroCriado = new Livro();

            List<Aluguel> alugueis = new ArrayList<>();

            Aluguel aluguelCriado = criarAluguel(usuarioCriado, livroCriado);

            alugueis.add(aluguelCriado);

            given(aluguelRepository.findAll()).willReturn(alugueis);

            List<AluguelResponseDTO> alugueisConsultados = aluguelService.consultarAlugueis();

            assertNotNull(alugueisConsultados);
            assertEquals(1, alugueisConsultados.size());

            then(aluguelRepository).should().findAll();

        }

        @Test
        void deveBuscarAlugueisDoUsuarioQuandoNaoForAdmin () {
            Usuario usuarioCriado = new Usuario();
            usuarioCriado.setRole(UserRole.USER);
            doReturn(usuarioCriado).when(aluguelService).obterUsuarioLogado();

            Livro livroCriado = new Livro();

            Aluguel aluguelCriado = criarAluguel(usuarioCriado, livroCriado);

            List<Aluguel> alugueis = new ArrayList<>();
            alugueis.add(aluguelCriado);


            given(aluguelRepository.findByUsuario(usuarioCriado))
                    .willReturn(alugueis);

            List<AluguelResponseDTO> alugueisConsultados = aluguelService.consultarAlugueis();

            assertNotNull(alugueisConsultados);
            assertEquals(1, alugueisConsultados.size());

            then(aluguelRepository).should().findByUsuario(usuarioCriado);
        }

        @Test
        void deveConverterAluguelParaAluguelResponseDTO () {

            Livro livroCriado = new Livro();
            livroCriado.setNome("O Pequeno Principe");

            Usuario usuarioCriado = new Usuario();
            usuarioCriado.setNome("Kayque");

            Aluguel aluguelCriado = new Aluguel();
            aluguelCriado.setId(1L);
            aluguelCriado.setLivro(livroCriado);
            aluguelCriado.setUsuario(usuarioCriado);
            aluguelCriado.setStatus(StatusAluguelEnum.ATIVO);
            aluguelCriado.setDataInicio(LocalDateTime.now());
            aluguelCriado.setPrazoDevolucao(LocalDate.now().plusWeeks(2));
            aluguelCriado.setEncerradoEm(null);

            AluguelResponseDTO response = aluguelService.convertToAluguelResponseDTO(aluguelCriado);

            assertNotNull(response);
            assertEquals(aluguelCriado.getId(), response.getId());
            assertEquals(aluguelCriado.getLivro().getNome(), response.getLivro());
            assertEquals(aluguelCriado.getUsuario().getNome(), response.getUsuario());
            assertEquals(aluguelCriado.getStatus(), response.getStatusAluguel());
            assertEquals(aluguelCriado.getDataInicio(), response.getDataInicio());
            assertEquals(aluguelCriado.getPrazoDevolucao(), response.getPrazoDevolucao());
            assertEquals(aluguelCriado.getEncerradoEm(), response.getEncerradoEm());

        }
}