package com.diacono.diacono.cadastro.controller;

import com.diacono.diacono.Igreja.model.dto.response.IgrejaSemiCompletoDTO;
import com.diacono.diacono.cadastro.model.dto.CadastroExternoDTO;
import com.diacono.diacono.cadastro.service.CadastroService;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CadastroControllerTest {

    @Mock
    private CadastroService cadastroService;

    @InjectMocks
    private CadastroController cadastroController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CadastroExternoDTO cadastroExternoDTO;
    private IgrejaSemiCompletoDTO igrejaSemiCompletoDTO1;
    private IgrejaSemiCompletoDTO igrejaSemiCompletoDTO2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cadastroController).build();
        objectMapper = new ObjectMapper();

        cadastroExternoDTO = new CadastroExternoDTO(
            UUID.randomUUID(),
            "João Silva",
            "11144477735", // CPF válido
            null,
            "joao@email.com",
            "11999999999",
            "123456",
            null
        );

        igrejaSemiCompletoDTO1 = new IgrejaSemiCompletoDTO(
            UUID.randomUUID(),
            "Igreja Batista Central"
        );

        igrejaSemiCompletoDTO2 = new IgrejaSemiCompletoDTO(
            UUID.randomUUID(),
            "Igreja Metodista"
        );
    }

    @Test
    @DisplayName("Deve buscar igrejas com sucesso")
    void buscarIgrejasSucesso() throws Exception {
        List<IgrejaSemiCompletoDTO> igrejas = Arrays.asList(igrejaSemiCompletoDTO1, igrejaSemiCompletoDTO2);

        when(cadastroService.buscarIgrejas()).thenReturn(igrejas);

        mockMvc.perform(get("/register")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idExterno").value(igrejaSemiCompletoDTO1.idExterno().toString()))
                .andExpect(jsonPath("$[0].nome").value(igrejaSemiCompletoDTO1.nome()))
                .andExpect(jsonPath("$[1].idExterno").value(igrejaSemiCompletoDTO2.idExterno().toString()))
                .andExpect(jsonPath("$[1].nome").value(igrejaSemiCompletoDTO2.nome()));

        verify(cadastroService).buscarIgrejas();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há igrejas")
    void buscarIgrejasListaVazia() throws Exception {
        List<IgrejaSemiCompletoDTO> igrejasVazias = Collections.emptyList();

        when(cadastroService.buscarIgrejas()).thenReturn(igrejasVazias);

        mockMvc.perform(get("/register")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(cadastroService).buscarIgrejas();
    }

    @Test
    @DisplayName("Deve cadastrar membro com sucesso")
    void cadastrarMembroSucesso() throws Exception {
        RestResponseMessage response = new RestResponseMessage(HttpStatus.CREATED, "Usuário cadastrado com sucesso");

        when(cadastroService.cadastrarMembro(any(CadastroExternoDTO.class))).thenReturn(response);

        String requestBody = objectMapper.writeValueAsString(cadastroExternoDTO);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.message").value("Usuário cadastrado com sucesso"));

        verify(cadastroService).cadastrarMembro(any(CadastroExternoDTO.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando dados de cadastro são nulos")
    void cadastrarMembroDadosNulosDeveRetornarErro() throws Exception {
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());

        verify(cadastroService, never()).cadastrarMembro(any(CadastroExternoDTO.class));
    }

    @Test
    @DisplayName("Deve validar dados obrigatórios no cadastro")
    void cadastrarMembroDadosObrigatoriosDeveValidar() throws Exception {
        String requestBodyInvalido = """
            {
                "fkIgreja": "%s",
                "cpf": "11144477735",
                "email": "joao@email.com",
                "celular": "11999999999",
                "senha": "123456"
            }
            """.formatted(UUID.randomUUID());

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyInvalido))
                .andExpect(status().isBadRequest());

        verify(cadastroService, never()).cadastrarMembro(any(CadastroExternoDTO.class));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando email é inválido")
    void cadastrarMembroEmailInvalidoDeveRetornarErro() throws Exception {
        CadastroExternoDTO cadastroInvalido = new CadastroExternoDTO(
            UUID.randomUUID(),
            "João Silva",
            "11144477735", // CPF válido
            null,
            "email-invalido", // Email inválido
            "11999999999",
            "123456",
            null
        );

        String requestBody = objectMapper.writeValueAsString(cadastroInvalido);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(cadastroService, never()).cadastrarMembro(any(CadastroExternoDTO.class));
    }
}