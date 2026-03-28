package com.biblioteca_icpi.controller;

import com.biblioteca_icpi.dto.EditarUsuarioDTO;
import com.biblioteca_icpi.dto.PromoverUsuarioDTO;
import com.biblioteca_icpi.dto.ResponseUsuarioDTO;
import com.biblioteca_icpi.exception.usuario.UsuarioNaoEncontradoException;
import com.biblioteca_icpi.infra.security.TokenService;
import com.biblioteca_icpi.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioService usuarioService;


    @Test
    void deveRetornarSucessoAoEditarUsuario () throws Exception {

        String json = """
                {
                "nome": "Kayque Santos",
                "senha": "senha-nova"
                }
                """;
        ResponseUsuarioDTO response = new ResponseUsuarioDTO();
        response.setNome("Kayque Santos");

        given(usuarioService.editarUsuario(eq(1L), any(EditarUsuarioDTO.class)))
                .willReturn(response);

        mvc.perform(
                put("/usuarios/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Kayque Santos"));
    }

    @Test
    void deveRetornarFalhaParaEditarUsuarioQuandoUsuarioNaoEncontrado () throws Exception {
        String json = """
                {
                "nome": "Kayque Santos",
                "senha": "senha-nova"
                }
                """;
        given(usuarioService.editarUsuario(eq(1L), any(EditarUsuarioDTO.class)))
                .willThrow(new UsuarioNaoEncontradoException("Usuário não encontrado!"));

        mvc.perform(
                        put("/usuarios/1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarFalhaAoTentarEditarUsuarioSemPermissao () throws Exception {
        String json = """
                {
                "nome": "Kayque Santos",
                "senha": "senha-nova"
                }
                """;

        given(usuarioService.editarUsuario(eq(1L), any(EditarUsuarioDTO.class)))
                .willThrow(new AccessDeniedException("Você não tem permissão para editar esse usuário"));

        mvc.perform(
                put("/usuarios/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveRetornarSucessoAoExcluirUsuario () throws Exception {


       mvc.perform(delete("/usuarios/1"))
               .andExpect(status().isNoContent());

       then(usuarioService).should().excluirUsuario(1L);

    }

    @Test
    void deveRetornarFalhaAoTentarExcluirUsuarioSemPermissao () throws Exception {
        
        doThrow(new AccessDeniedException("Você não tem permissão para excluir esse usuário"))
                .when(usuarioService)
                .excluirUsuario(1L);

        mvc.perform(delete("/usuarios/1"))
                .andExpect(status().isForbidden());

        then(usuarioService).should().excluirUsuario(1L);

    }

    @Test
    void deveRetornarFalhaAoTentarExcluirUsuarioSemEncontrarUsuario () throws Exception {
        doThrow(new UsuarioNaoEncontradoException("Usuário não encontrado!"))
                .when(usuarioService).excluirUsuario(1L);

        mvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNotFound());

        then(usuarioService).should().excluirUsuario(1L);

    }

    @Test
    void deveRetornarFalhaAoTentarExcluirUsuarioComHistoricoDeAluguel () throws Exception {
        doThrow(new IllegalStateException("Usuário possui histórico de aluguel e não pode ser excluído"))
                .when(usuarioService).excluirUsuario(1L);

        mvc.perform(delete("/usuarios/1"))
                .andExpect(status().isConflict());

        then(usuarioService).should().excluirUsuario(1L);
    }

    @Test
    void deveRetornarSucessoAoConsultarUsuario () throws Exception {

        ResponseUsuarioDTO response = new ResponseUsuarioDTO();
        response.setId(1L);

        given(usuarioService.buscarUsuarioNoBanco(1L))
                .willReturn(response);

        mvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk());

        then(usuarioService).should().buscarUsuarioNoBanco(1L);
    }

    @Test
    void deveRetornarSucessoAoConsultarUsuarios () throws Exception {

        ResponseUsuarioDTO usuario1 = new ResponseUsuarioDTO();
        ResponseUsuarioDTO usuario2 = new ResponseUsuarioDTO();

        List<ResponseUsuarioDTO> lista = new ArrayList<>();
        lista.add(usuario1);
        lista.add(usuario2);


        given(usuarioService.listarUsuarios())
                .willReturn(lista);

        mvc.perform(get("/usuarios"))
                .andExpect(status().isOk());

        then(usuarioService).should().listarUsuarios();
    }

    @Test
    void deveRetornarSucessoAoPromoverParaAdmin () throws Exception {

        ResponseUsuarioDTO response = new ResponseUsuarioDTO();
        response.setEmail("kayque.teste@email.com");

        String json = """
                {
                "email": "kayque.teste@email.com"
                }
                
                """;

        given(usuarioService.promoverParaAdmin("kayque.teste@email.com"))
                .willReturn(response);

        mvc.perform(put("/usuarios/promoverAdmin")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("kayque.teste@email.com"));

        then(usuarioService).should().promoverParaAdmin("kayque.teste@email.com");
    }





}