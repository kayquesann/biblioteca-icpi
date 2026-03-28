package com.biblioteca_icpi.service;

import com.biblioteca_icpi.dto.CadastrarUsuarioDTO;
import com.biblioteca_icpi.dto.EditarUsuarioDTO;
import com.biblioteca_icpi.dto.ResponseUsuarioDTO;
import com.biblioteca_icpi.exception.usuario.UsuarioJaExistenteException;
import com.biblioteca_icpi.exception.usuario.UsuarioNaoEncontradoException;
import com.biblioteca_icpi.model.Aluguel;
import com.biblioteca_icpi.model.UserRole;
import com.biblioteca_icpi.model.Usuario;
import com.biblioteca_icpi.repository.AluguelRepository;
import com.biblioteca_icpi.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    @Spy
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private Aluguel aluguel;

    @Captor
    private ArgumentCaptor<Usuario> captor;


    @Test
    void deveBuscarUsuarioNoBancoSendoAdmin () throws AccessDeniedException {

        Long id = 1L;

        List<Aluguel> alugueisCriados = new ArrayList<>();
        alugueisCriados.add(aluguel);

        Usuario usuarioCriado = new Usuario();
        usuarioCriado.setId(1L);
        usuarioCriado.setNome("Kayque");
        usuarioCriado.setRole(UserRole.ADMIN);
        usuarioCriado.setEmail("kayque@teste.com");
        usuarioCriado.setSenha("teste123");
        usuarioCriado.setAlugueis(alugueisCriados);

        Usuario usuarioCriado2 = new Usuario();
        usuarioCriado2.setId(id);

        doReturn(usuarioCriado).when(usuarioService).obterUsuarioLogado();

        given(usuarioRepository.findById(id)).willReturn(Optional.of(usuarioCriado2));

        ResponseUsuarioDTO response = usuarioService.buscarUsuarioNoBanco(id);

        assertNotNull(response);
        assertDoesNotThrow(() -> usuarioService.buscarUsuarioNoBanco(id));
    }

    @Test
    void deveBuscarUsuarioNoBancoSendoUser () throws AccessDeniedException {

        Long id = 1L;

        List<Aluguel> alugueisCriados = new ArrayList<>();
        alugueisCriados.add(aluguel);

        Usuario usuarioCriado = new Usuario();
        usuarioCriado.setId(id);
        usuarioCriado.setNome("Kayque");
        usuarioCriado.setRole(UserRole.USER);
        usuarioCriado.setEmail("kayque@teste.com");
        usuarioCriado.setSenha("teste123");
        usuarioCriado.setAlugueis(alugueisCriados);


        doReturn(usuarioCriado).when(usuarioService).obterUsuarioLogado();

        given(usuarioRepository.findById(id)).willReturn(Optional.of(usuarioCriado));

        ResponseUsuarioDTO response = usuarioService.buscarUsuarioNoBanco(id);

        assertNotNull(response);
        assertDoesNotThrow(() -> usuarioService.buscarUsuarioNoBanco(id));
    }

    @Test
    void naoDevePermitirBuscaDeUsuario () throws AccessDeniedException {
        Long id = 2L;

        List<Aluguel> alugueisCriados = new ArrayList<>();
        alugueisCriados.add(aluguel);

        Usuario usuarioCriado = new Usuario();
        usuarioCriado.setId(1L);
        usuarioCriado.setNome("Kayque");
        usuarioCriado.setRole(UserRole.USER);
        usuarioCriado.setEmail("kayque@teste.com");
        usuarioCriado.setSenha("teste123");
        usuarioCriado.setAlugueis(alugueisCriados);

        Usuario usuarioCriado2 = new Usuario();
        usuarioCriado2.setId(id);

        doReturn(usuarioCriado).when(usuarioService).obterUsuarioLogado();

        assertThrows(AccessDeniedException.class,
                () -> usuarioService.buscarUsuarioNoBanco(id));
    }

    @Test
    void deveCriarUsuario () {

        CadastrarUsuarioDTO cadastrarUsuarioDTO = new CadastrarUsuarioDTO();
        cadastrarUsuarioDTO.setNome("Kayque");
        cadastrarUsuarioDTO.setEmail("kayque.teste@email.com");
        cadastrarUsuarioDTO.setSenha("teste123");

        given(usuarioRepository.findByEmail(cadastrarUsuarioDTO.getEmail()))
                .willReturn(Optional.empty());

        given(passwordEncoder.encode(cadastrarUsuarioDTO.getSenha()))
                .willReturn("senha-criptografada");


        ResponseUsuarioDTO response = usuarioService.criarUsuario(cadastrarUsuarioDTO);
        then(usuarioRepository).should().save(captor.capture());

        Usuario usuarioCapturado = captor.getValue();

        assertNotNull(response);
        then(usuarioRepository).should().findByEmail(cadastrarUsuarioDTO.getEmail());
        then(passwordEncoder).should().encode(cadastrarUsuarioDTO.getSenha());
        assertEquals(usuarioCapturado.getNome(), cadastrarUsuarioDTO.getNome());
        assertEquals(usuarioCapturado.getEmail(), cadastrarUsuarioDTO.getEmail());
        assertEquals(usuarioCapturado.getSenha(), "senha-criptografada");
        assertEquals(usuarioCapturado.getRole(), UserRole.USER);
        assertNull(usuarioCapturado.getAlugueis());

    }

    @Test
    void naoDeveCriarUsuarioPoisJaExisteEsseUsuario () {
        CadastrarUsuarioDTO cadastrarUsuarioDTO = new CadastrarUsuarioDTO();
        cadastrarUsuarioDTO.setNome("Kayque");
        cadastrarUsuarioDTO.setEmail("kayque.teste@email.com");
        cadastrarUsuarioDTO.setSenha("teste123");

        Usuario usuarioCriado = new Usuario();
        usuarioCriado.setEmail("kayque.teste@email.com");

        given(usuarioRepository.findByEmail(cadastrarUsuarioDTO.getEmail()))
                .willReturn(Optional.of(usuarioCriado));


        assertThrows(UsuarioJaExistenteException.class, () -> usuarioService.criarUsuario(cadastrarUsuarioDTO));

        then(usuarioRepository).should().findByEmail(cadastrarUsuarioDTO.getEmail());
    }

    @Test
    void devePromoverParaAdmin () {
        String email = "kayque.teste@email.com";

        Usuario usuarioCriado = new Usuario();
        usuarioCriado.setEmail(email);

        given(usuarioRepository.findByEmail(email))
                .willReturn(Optional.of(usuarioCriado));

        ResponseUsuarioDTO response = usuarioService.promoverParaAdmin(email);

        then(usuarioRepository).should().save(usuarioCriado);
        assertNotNull(response);
        assertEquals(UserRole.ADMIN, response.getRole());
    }

    @Test
    void naoDevePromoverParaAdmin () {
        String email = "kayque.teste@email.com";

        given(usuarioRepository.findByEmail(email))
                .willReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.promoverParaAdmin(email));
    }

    @Test
    void deveEditarUsuarioSendoAdmin () throws AccessDeniedException {
        Long id = 1L;

        EditarUsuarioDTO dto = new EditarUsuarioDTO();
        dto.setNome("Kayque Santos");
        dto.setSenha("teste1234");

        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setRole(UserRole.ADMIN);

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setSenha("hello");
        usuarioEncontrado.setId(id);

        doReturn(usuarioLogado).when(usuarioService).obterUsuarioLogado();

        given(usuarioRepository.findById(id))
                .willReturn(Optional.of(usuarioEncontrado));

        given(passwordEncoder.encode(dto.getSenha()))
                .willReturn("senha-criptografada");



        ResponseUsuarioDTO response = usuarioService.editarUsuario(id, dto);

        then(usuarioRepository).should().findById(id);
        then(passwordEncoder).should().encode(dto.getSenha());
        then(usuarioRepository).should().save(captor.capture());
        Usuario usuarioSalvo = captor.getValue();


        assertEquals(dto.getNome(), usuarioSalvo.getNome());
        assertEquals("senha-criptografada", usuarioSalvo.getSenha());

        assertNotNull(response);
        assertEquals(dto.getNome(), response.getNome());
    }

    @Test
    void devoEditarUsuarioSendoOMeu () throws AccessDeniedException {
    Long id = 1L;

    EditarUsuarioDTO dto = new EditarUsuarioDTO();
    dto.setNome("Kayque Santos");
    dto.setSenha("senha-nova");

    Usuario usuarioLogado = new Usuario();
    usuarioLogado.setRole(UserRole.USER);
    usuarioLogado.setId(id);

    doReturn(usuarioLogado).when(usuarioService).obterUsuarioLogado();
    given(usuarioRepository.findById(id))
            .willReturn(Optional.of(usuarioLogado));

    given(passwordEncoder.encode(dto.getSenha()))
            .willReturn("senha-criptografada");

    ResponseUsuarioDTO response = usuarioService.editarUsuario(id, dto);

    then(usuarioRepository).should().save(captor.capture());
    then(usuarioRepository).should().findById(id);
    then(passwordEncoder).should().encode(dto.getSenha());

    Usuario usuarioSalvo = captor.getValue();

    assertEquals(dto.getNome(), usuarioSalvo.getNome());
    assertEquals("senha-criptografada", usuarioSalvo.getSenha());
    assertNotNull(response);

    }

    @Test
    void naoDevePermitirEdicaoPorFaltaDePermissao () throws AccessDeniedException {
        Long id = 1L;

        EditarUsuarioDTO dto = new EditarUsuarioDTO();

        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setId(2L);

        doReturn(usuarioLogado).when(usuarioService).obterUsuarioLogado();


        assertThrows(AccessDeniedException.class, () -> usuarioService.editarUsuario(id, dto));
    }

    @Test
    void naoDeveEncontrarUsuarioAoTentarEditar () {
        Long id = 1L;
        EditarUsuarioDTO dto = new EditarUsuarioDTO();

        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setId(2L);
        usuarioLogado.setRole(UserRole.ADMIN);

        doReturn(usuarioLogado).when(usuarioService).obterUsuarioLogado();

        given(usuarioRepository.findById(id))
                .willReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.editarUsuario(id, dto));
    }

    @Test
    void DeveExcluirUsuarioSendoAdmin () throws AccessDeniedException {
        Long id = 1L;

        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setRole(UserRole.ADMIN);

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(id);

        doReturn(usuarioLogado).when(usuarioService).obterUsuarioLogado();

        given(usuarioRepository.findById(id))
                .willReturn(Optional.of(usuarioEncontrado));

        given(aluguelRepository.existsByUsuario(usuarioEncontrado))
                .willReturn(false);

        usuarioService.excluirUsuario(id);

        then(usuarioService).should().obterUsuarioLogado();
        then(usuarioRepository).should().findById(id);
        then(aluguelRepository.existsByUsuario(usuarioEncontrado));
        then(usuarioRepository).should().delete(usuarioEncontrado);
    }

    @Test
    void DevoExcluirMeuUsuario () throws AccessDeniedException {
        Long id = 1L;

        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setRole(UserRole.USER);
        usuarioLogado.setId(id);

        doReturn(usuarioLogado).when(usuarioService).obterUsuarioLogado();

        given(usuarioRepository.findById(id))
                .willReturn(Optional.of(usuarioLogado));

        given(aluguelRepository.existsByUsuario(usuarioLogado))
                .willReturn(false);

        usuarioService.excluirUsuario(id);

        then(usuarioService).should().obterUsuarioLogado();
        then(usuarioRepository).should().findById(id);
        then(aluguelRepository).should().existsByUsuario(usuarioLogado);
        then(usuarioRepository).should().delete(usuarioLogado);
    }

    @Test
    void naoDevoTerPermissaoParaExcluir () throws AccessDeniedException {
        Long id = 1L;

        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setRole(UserRole.USER);
        usuarioLogado.setId(2L);

        doReturn(usuarioLogado).when(usuarioService).obterUsuarioLogado();

        assertThrows(AccessDeniedException.class, () -> usuarioService.excluirUsuario(id));
    }

    @Test
    void naoDevoConseguirExcluirPorNaoEncontrarOUsuario () {
        Long id = 1L;

        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setRole(UserRole.ADMIN);
        usuarioLogado.setId(2L);

        doReturn(usuarioLogado).when(usuarioService).obterUsuarioLogado();

        given(usuarioRepository.findById(id))
                .willReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.excluirUsuario(id));
    }

    @Test
    void naoDevoConseguirExcluirPorTerAluguelAtivo () {
        Long id = 1L;

        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setRole(UserRole.ADMIN);
        usuarioLogado.setId(2L);

        doReturn(usuarioLogado).when(usuarioService).obterUsuarioLogado();

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(id);

        given(usuarioRepository.findById(id))
                .willReturn(Optional.of(usuarioEncontrado));

        given(aluguelRepository.existsByUsuario(usuarioEncontrado))
                .willReturn(true);

        assertThrows(IllegalStateException.class, () -> usuarioService.excluirUsuario(id));
    }

    @Test
    void deveListarUsuarios () {

        Usuario primeiroUsuario = new Usuario();
        primeiroUsuario.setId(1L);
        primeiroUsuario.setNome("Kayque");

        Usuario segundoUsuario = new Usuario();
        segundoUsuario.setId(2L);
        segundoUsuario.setNome("João");

        List<Usuario> listaDeUsuarios = new ArrayList<>();

        listaDeUsuarios.add(primeiroUsuario);
        listaDeUsuarios.add(segundoUsuario);

        given(usuarioRepository.findAll())
                .willReturn(listaDeUsuarios);

        List<ResponseUsuarioDTO> response = usuarioService.listarUsuarios();

        then(usuarioRepository).should().findAll();

        assertEquals(2, response.size());
        assertEquals("Kayque", response.get(0).getNome());
        assertEquals("João", response.get(1).getNome());
    }

    @Test
    void deveConverterUsuarioParaResponseUsuarioDTO () {
        Usuario usuario = new Usuario();
        usuario.setNome("Kayque");
        usuario.setRole(UserRole.USER);
        usuario.setEmail("kayque@teste.com");
        usuario.setId(1L);

        ResponseUsuarioDTO response = usuarioService.convertToResponseUsuarioDTO(usuario);

        assertNotNull(response);
        assertEquals(usuario.getNome(), response.getNome());
        assertEquals(usuario.getRole(), response.getRole());
        assertEquals(usuario.getEmail(), response.getEmail());
        assertEquals(usuario.getId(), response.getId());
    }

}