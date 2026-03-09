package com.diacono.diacono.auth.service;

import com.diacono.diacono.applications.dtos.login.LoginRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.use_cases.LoginService;
import com.diacono.diacono.use_cases.MembroService;
import com.diacono.diacono.use_cases.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private MembroService membroService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private LoginService loginService;

    @Test
    @DisplayName("Deve validar login com sucesso e retornar token")
    void validarLoginSucesso() {
        String email = "usuario@teste.com";
        String senha = "senha123";
        String hashed = "senhaHasheada";

        LoginRequestDTO request = new LoginRequestDTO(email, senha);

        Membro membro = new Membro();
        membro.setEmail(email);
        membro.setSenha(hashed);

        when(membroService.buscarPorEmail(email)).thenReturn(membro);
        when(bCryptPasswordEncoder.matches(senha, hashed)).thenReturn(true);
        when(tokenService.generateToken(membro)).thenReturn("jwt-token");
        when(tokenService.getExpiresIn()).thenReturn(3600L);

        LoginResponseDTO response = loginService.validarLogin(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.acessToken());
        assertEquals(3600L, response.expiresIn());

        verify(membroService).buscarPorEmail(email);
        verify(bCryptPasswordEncoder).matches(senha, hashed);
        verify(tokenService).generateToken(membro);
        verify(tokenService).getExpiresIn();
    }

    @Test
    @DisplayName("Deve lançar erro quando membro não é encontrado")
    void validarLoginMembroNaoEncontradoDeveRetornarErro() {
        String email = "naoexiste@teste.com";
        String senha = "senha";

        LoginRequestDTO request = new LoginRequestDTO(email, senha);

        when(membroService.buscarPorEmail(email)).thenReturn(null);

        assertThrows(BadCredentialsException.class, () -> loginService.validarLogin(request));

        verify(membroService).buscarPorEmail(email);
        verifyNoInteractions(bCryptPasswordEncoder, tokenService);
    }

    @Test
    @DisplayName("Deve lançar erro quando senha estiver inválida")
    void validarLoginSenhaInvalidaDeveRetornarErro() {
        String email = "usuario@teste.com";
        String senha = "senhaErrada";
        String hashed = "senhaHasheada";

        LoginRequestDTO request = new LoginRequestDTO(email, senha);

        Membro membro = new Membro();
        membro.setEmail(email);
        membro.setSenha(hashed);

        when(membroService.buscarPorEmail(email)).thenReturn(membro);
        when(bCryptPasswordEncoder.matches(senha, hashed)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> loginService.validarLogin(request));

        verify(membroService).buscarPorEmail(email);
        verify(bCryptPasswordEncoder).matches(senha, hashed);
        verifyNoInteractions(tokenService);
    }
}