package com.diacono.diacono.auth.controller;

import com.diacono.diacono.auth.model.dto.request.LoginRequestDTO;
import com.diacono.diacono.auth.model.dto.response.LoginResponseDTO;
import com.diacono.diacono.auth.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private LoginRequestDTO loginRequestDTO;
    private LoginResponseDTO loginResponseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        objectMapper = new ObjectMapper();

        loginRequestDTO = new LoginRequestDTO(
            "joao@email.com",
            "123456"
        );

        loginResponseDTO = new LoginResponseDTO(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            3600L
        );
    }

    @Test
    @DisplayName("Deve realizar login com sucesso")
    void loginSucesso() throws Exception {
        when(loginService.validarLogin(any(LoginRequestDTO.class))).thenReturn(loginResponseDTO);

        String requestBody = objectMapper.writeValueAsString(loginRequestDTO);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acessToken").value(loginResponseDTO.acessToken()))
                .andExpect(jsonPath("$.expiresIn").value(loginResponseDTO.expiresIn()));

        verify(loginService).validarLogin(any(LoginRequestDTO.class));
    }

    //  Não deve aceitar requisição com email ausente, pois o email é obrigatório

//    @Test
//    @DisplayName("Deve aceitar requisição com email ausente e passar para o serviço")
//    void loginEmailAusentePassaParaServico() throws Exception {
//        LoginResponseDTO responseComErro = new LoginResponseDTO(null, null);
//        when(loginService.validarLogin(any(LoginRequestDTO.class))).thenReturn(responseComErro);
//
//        String requestBodySemEmail = """
//            {
//                "senha": "123456"
//            }
//            """;
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBodySemEmail))
//                .andExpect(status().isOk());
//
//        verify(loginService).validarLogin(any(LoginRequestDTO.class));
//    }

    // Não deve aceitar requisição com senha ausente, pois a senha é obrigatória

//    @Test
//    @DisplayName("Deve aceitar requisição com senha ausente e passar para o serviço")
//    void loginSenhaAusentePassaParaServico() throws Exception {
//        LoginResponseDTO responseComErro = new LoginResponseDTO(null, null);
//        when(loginService.validarLogin(any(LoginRequestDTO.class))).thenReturn(responseComErro);
//
//        String requestBodySemSenha = """
//            {
//                "email": "joao@email.com"
//            }
//            """;
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBodySemSenha))
//                .andExpect(status().isOk());
//
//        verify(loginService).validarLogin(any(LoginRequestDTO.class));
//    }

    @Test
    @DisplayName("Deve validar Content-Type no login")
    void loginContentTypeDeveValidar() throws Exception {
        String requestBody = objectMapper.writeValueAsString(loginRequestDTO);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.TEXT_PLAIN)
                .content(requestBody))
                .andExpect(status().isUnsupportedMediaType());

        verify(loginService, never()).validarLogin(any(LoginRequestDTO.class));
    }

    @Test
    @DisplayName("Deve chamar o serviço de login com dados corretos")
    void loginChamaServicoCorretamente() throws Exception {
        when(loginService.validarLogin(any(LoginRequestDTO.class))).thenReturn(loginResponseDTO);

        String requestBody = objectMapper.writeValueAsString(loginRequestDTO);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        verify(loginService, times(1)).validarLogin(any(LoginRequestDTO.class));
    }

    @Test
    @DisplayName("Deve retornar status 200 OK para login válido")
    void loginValidoRetornaStatusOk() throws Exception {
        when(loginService.validarLogin(any(LoginRequestDTO.class))).thenReturn(loginResponseDTO);

        String requestBody = objectMapper.writeValueAsString(loginRequestDTO);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(loginService).validarLogin(any(LoginRequestDTO.class));
    }
}