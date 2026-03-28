package com.biblioteca_icpi.controller;

import com.biblioteca_icpi.dto.CadastrarLivroDTO;
import com.biblioteca_icpi.dto.LivroResponseDTO;
import com.biblioteca_icpi.exception.livro.LivroJaExistenteException;
import com.biblioteca_icpi.exception.livro.LivroNaoEncontradoException;
import com.biblioteca_icpi.infra.security.TokenService;
import com.biblioteca_icpi.model.Livro;
import com.biblioteca_icpi.service.LivroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class LivroControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private LivroService livroService;

    @Test
    void deveBuscarLivroComSucesso () throws Exception {

        LivroResponseDTO livro = new LivroResponseDTO();
        livro.setId(1L);

        given(livroService.buscarLivroNoBancoDeDados(1L))
                .willReturn(livro);

        mvc.perform(get("/livros/1"))
                .andExpect(status().isOk());

        then(livroService).should().buscarLivroNoBancoDeDados(1L);
    }

    @Test
    void deveCadastrarLivroComSucesso () throws Exception {

        LivroResponseDTO livroResponseDTO = new LivroResponseDTO();
        livroResponseDTO.setAutor("Antoine");

        String json = """
                {
                "nome": "O Pequeno Principe",
                "autor": "Antoine",
                "descricao": "um livro legal",
                "genero": "Ficção"
                }
                
                """ ;

        given(livroService.cadastrarLivro(any(CadastrarLivroDTO.class)))
                .willReturn(livroResponseDTO);

        mvc.perform(post("/livros")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.autor").value("Antoine"))
                .andExpect(status().isCreated());

        then(livroService).should().cadastrarLivro(any(CadastrarLivroDTO.class));
    }

    @Test
    void deveRetornarErroAoTentarCadastrarLivroExistente () throws Exception {

        String json = """
                {
                "nome": "O Pequeno Principe",
                "autor": "Antoine",
                "descricao": "um livro legal",
                "genero": "Ficção"
                }
                
                """ ;

        given(livroService.cadastrarLivro(any(CadastrarLivroDTO.class)))
                .willThrow(new LivroJaExistenteException("Livro já cadastrado!"));

        mvc.perform(post("/livros")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        then(livroService).should().cadastrarLivro(any(CadastrarLivroDTO.class));
    }

    @Test
    void deveEditarLivro () throws Exception {

        LivroResponseDTO livro = new LivroResponseDTO();
        livro.setGenero("Infantil");

        String json = """
                {
                "nome": "O Pequeno Principe",
                "autor": "Antoine",
                "descricao": "um livro bacana",
                "genero": "Infantil"
                }
                
                """ ;

        given(livroService.editarLivro(eq(1L), any(CadastrarLivroDTO.class)))
                .willReturn(livro);

        mvc.perform(put("/livros/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genero").value("Infantil"));

        then(livroService).should().editarLivro(eq(1L),any(CadastrarLivroDTO.class));
    }

    @Test
    void naoDeveEncontrarLivroPraEditar () throws Exception {

        given(livroService.editarLivro(eq(1L), any(CadastrarLivroDTO.class)))
                .willThrow(new LivroNaoEncontradoException("Livro não encontrado!"));

        String json = """
                {
                "nome": "O Pequeno Principe",
                "autor": "Antoine",
                "descricao": "um livro bacana",
                "genero": "Infantil"
                }
                
                """ ;

        mvc.perform(put("/livros/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        then(livroService).should().editarLivro(eq(1L), any(CadastrarLivroDTO.class));
    }

    @Test
    void deveDeletarLivro () throws Exception {

        mvc.perform(delete("/livros/1"))
                .andExpect(status().isNoContent());

        then(livroService).should().excluirLivro(1L);

    }

    @Test
    void naoDevePermitirDeletarLivroComHistoricoDeAluguel () throws Exception {

        doThrow(new IllegalStateException("O livro não pode ser deletado pois possui histórico de alugueis"))
                .when(livroService)
                .excluirLivro(1L);

        mvc.perform(delete("/livros/1"))
                .andExpect(status().isConflict());

        then(livroService).should().excluirLivro(1L);
    }

    @Test
    void naoDevePermitirDeletarLivroNaoEncontrado () throws Exception {
        doThrow(new LivroNaoEncontradoException("Livro não encontrado!"))
                .when(livroService)
                .excluirLivro(1L);

        mvc.perform(delete("/livros/1"))
                .andExpect(status().isNotFound());

        then(livroService).should().excluirLivro(1L);
    }

    @Test
    void deveRetornarLivros () throws Exception {

        LivroResponseDTO livro1 = new LivroResponseDTO();
        livro1.setNome("As Crônicas de Nárnia");


        LivroResponseDTO livro2 = new LivroResponseDTO();
        livro2.setNome("Harry Potter");

        List<LivroResponseDTO> livros = new ArrayList<>();
        livros.add(livro1);
        livros.add(livro2);

        given(livroService.listarLivros())
                .willReturn(livros);

        mvc.perform(get("/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("As Crônicas de Nárnia"))
                .andExpect(jsonPath("$[1].nome").value("Harry Potter"));

        then(livroService).should().listarLivros();
    }




}