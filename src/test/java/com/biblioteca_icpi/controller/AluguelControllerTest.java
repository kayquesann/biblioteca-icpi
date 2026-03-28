package com.biblioteca_icpi.controller;

import com.biblioteca_icpi.dto.AlugarDTO;
import com.biblioteca_icpi.dto.AluguelResponseDTO;
import com.biblioteca_icpi.exception.livro.LivroNaoEncontradoException;
import com.biblioteca_icpi.exception.usuario.UsuarioNaoEncontradoException;
import com.biblioteca_icpi.infra.security.TokenService;
import com.biblioteca_icpi.service.AluguelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AluguelControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private AluguelService aluguelService;


    @Test
    void deveAlugarLivro () throws Exception {

        String json = """
                {
                "idLivro": 1,
                "email": "kayque.teste@email.com"
                }
                """;

        AluguelResponseDTO response = new AluguelResponseDTO();

        given(aluguelService.alugarLivro(any(AlugarDTO.class)))
                .willReturn(response);

        mvc.perform(post("/alugueis")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        then(aluguelService).should().alugarLivro(any(AlugarDTO.class));
    }

    @Test
    void naoDeveAlugarLivroNaoEncontrado () throws Exception {
        String json = """
                {
                "idLivro": 1,
                "email": "kayque.teste@email.com"
                }
                """;
        given(aluguelService.alugarLivro(any(AlugarDTO.class)))
                .willThrow(new LivroNaoEncontradoException("Livro não encontrado"));

        mvc.perform(post("/alugueis")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        then(aluguelService).should().alugarLivro(any(AlugarDTO.class));
    }

    @Test
    void naoDeveAlugarLivroSemEncontrarUsuario () throws Exception {
        String json = """
                {
                "idLivro": 1,
                "email": "kayque.teste@email.com"
                }
                """;
        given(aluguelService.alugarLivro(any(AlugarDTO.class)))
                .willThrow(new UsuarioNaoEncontradoException("Usuário não encontrado"));

        mvc.perform(post("/alugueis")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        then(aluguelService).should().alugarLivro(any(AlugarDTO.class));
    }

    @Test
    void naoDeveAlugarLivroJaAlugado () throws Exception {
        String json = """
                {
                "idLivro": 1,
                "email": "kayque.teste@email.com"
                }
                """;
        given(aluguelService.alugarLivro(any(AlugarDTO.class)))
                .willThrow(new IllegalStateException("O livro já está alugado!"));

        mvc.perform(post("/alugueis")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        then(aluguelService).should().alugarLivro(any(AlugarDTO.class));
    }

    @Test
    void deveDevolverLivro () throws Exception {

        AluguelResponseDTO response = new AluguelResponseDTO();

        given(aluguelService.devolverLivro(1L))
                .willReturn(response);

        mvc.perform(put("/alugueis/1"))
                .andExpect(status().isOk());

        then(aluguelService).should().devolverLivro(1L);
    }

    @Test
    void naoDeveDevolverLivroSemEncontrarAluguel () throws Exception {

        given(aluguelService.devolverLivro(1L))
                .willThrow(new IllegalStateException("Aluguel não encontrado"));

        mvc.perform(put("/alugueis/1"))
                .andExpect(status().isConflict());

        then(aluguelService).should().devolverLivro(1L);
    }

    @Test
    void naoDeveDevolverLivroSeAluguelNaoEstiverAtivo () throws Exception {

        given(aluguelService.devolverLivro(1L))
                .willThrow(new IllegalStateException("Este aluguel já foi encerrado e o livro devolvido"));

        mvc.perform(put("/alugueis/1"))
                .andExpect(status().isConflict());

        then(aluguelService).should().devolverLivro(1L);
    }

    @Test
    void deveConsultarAluguel () throws Exception {

        AluguelResponseDTO response = new AluguelResponseDTO();

        given(aluguelService.consultarAluguelEspecifico(1L))
                .willReturn(response);

        mvc.perform(get("/alugueis/1"))
                .andExpect(status().isOk());

        then(aluguelService).should().consultarAluguelEspecifico(1L);
    }


    @Test
    void naoDeveConsultarAluguelNaoEncontrado () throws Exception {

        given(aluguelService.consultarAluguelEspecifico(1L))
                .willThrow(new IllegalStateException("Aluguel não encontrado"));

        mvc.perform(get("/alugueis/1"))
                .andExpect(status().isConflict());

        then(aluguelService).should().consultarAluguelEspecifico(1L);
    }

    @Test
    void naoDeveConsultarAluguelSemTerPermissao () throws Exception {

        given(aluguelService.consultarAluguelEspecifico(1L))
                .willThrow(new AccessDeniedException("Você não tem permissão para acessar este aluguel"));

        mvc.perform(get("/alugueis/1"))
                .andExpect(status().isForbidden());

        then(aluguelService).should().consultarAluguelEspecifico(1L);
    }

    @Test
    void deveConsultarAlugueis () throws Exception {

        List<AluguelResponseDTO> alugueis = new ArrayList<>();

        AluguelResponseDTO aluguel1 = new AluguelResponseDTO();
        aluguel1.setLivro("O Pequeno Principe");

        AluguelResponseDTO aluguel2 = new AluguelResponseDTO();
        aluguel2.setLivro("Harry Potter");

        alugueis.add(aluguel1);
        alugueis.add(aluguel2);


        given(aluguelService.consultarAlugueis())
                .willReturn(alugueis);

        mvc.perform(get("/alugueis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].livro").value("O Pequeno Principe")).
                andExpect(jsonPath("$[1].livro").value("Harry Potter"));


        then(aluguelService).should().consultarAlugueis();
    }





}