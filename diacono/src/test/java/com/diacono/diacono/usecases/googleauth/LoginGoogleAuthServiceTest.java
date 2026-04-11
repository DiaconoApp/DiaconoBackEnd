package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleAuthRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.domain.auth.GoogleIdTokenClaims;
import com.diacono.diacono.domain.auth.GoogleIdTokenVerifier;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.global.config.GoogleOAuthProperties;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.usecases.GenerateTokenUseCase;
import com.diacono.diacono.usecases.membro.BuscarPorEmaiUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginGoogleAuthServiceTest {

    @Mock
    private GoogleIdTokenVerifier googleIdTokenVerifier;

    @Mock
    private GenerateTokenUseCase generateTokenUseCase;

    @Mock
    private BuscarPorEmaiUseCase buscarPorEmaiUseCase;

    private LoginGoogleAuthService loginGoogleAuthService;

    @BeforeEach
    void setup() {
        GoogleOAuthProperties googleOAuthProperties = new GoogleOAuthProperties("google-client-id", "google-client-secret");
        loginGoogleAuthService = new LoginGoogleAuthService(
                googleIdTokenVerifier,
                generateTokenUseCase,
                buscarPorEmaiUseCase,
                googleOAuthProperties
        );
    }

    @Test
    void deveAutenticarQuandoTokenGoogleForValido() {
        GoogleAuthRequestDTO request = new GoogleAuthRequestDTO("id-token");
        GoogleIdTokenClaims googleClaims = new GoogleIdTokenClaims(
                List.of("google-client-id"),
                "usuario@teste.com",
                true
        );
        Membro membro = new Membro();

        when(googleIdTokenVerifier.verify(request.idToken())).thenReturn(googleClaims);
        when(buscarPorEmaiUseCase.execute("usuario@teste.com")).thenReturn(membro);
        when(generateTokenUseCase.execute(membro)).thenReturn("jwt-token");
        when(generateTokenUseCase.getExpiresIn()).thenReturn(3600L);

        LoginResponseDTO response = loginGoogleAuthService.autenticar(request);

        assertEquals("jwt-token", response.acessToken());
        assertEquals(3600L, response.expiresIn());
    }

    @Test
    void deveLancarExcecaoQuandoAudienceNaoPertencerAoClientDaAplicacao() {
        GoogleAuthRequestDTO request = new GoogleAuthRequestDTO("id-token");
        GoogleIdTokenClaims googleClaims = new GoogleIdTokenClaims(
                List.of("outro-client-id"),
                "usuario@teste.com",
                true
        );

        when(googleIdTokenVerifier.verify(request.idToken())).thenReturn(googleClaims);

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> loginGoogleAuthService.autenticar(request)
        );

        assertEquals("Token do Google nao pertence a aplicacao", exception.getMessage());
        verify(buscarPorEmaiUseCase, never()).execute(any());
    }

    @Test
    void deveLancarExcecaoQuandoEmailNaoForVerificado() {
        GoogleAuthRequestDTO request = new GoogleAuthRequestDTO("id-token");
        GoogleIdTokenClaims googleClaims = new GoogleIdTokenClaims(
                List.of("google-client-id"),
                "usuario@teste.com",
                false
        );

        when(googleIdTokenVerifier.verify(request.idToken())).thenReturn(googleClaims);

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> loginGoogleAuthService.autenticar(request)
        );

        assertEquals("Email do Google nao verificado", exception.getMessage());
        verify(buscarPorEmaiUseCase, never()).execute(any());
    }

    @Test
    void deveLancarExcecaoQuandoMembroNaoExistirNoSistema() {
        GoogleAuthRequestDTO request = new GoogleAuthRequestDTO("id-token");
        GoogleIdTokenClaims googleClaims = new GoogleIdTokenClaims(
                List.of("google-client-id"),
                "naoexiste@teste.com",
                true
        );

        when(googleIdTokenVerifier.verify(request.idToken())).thenReturn(googleClaims);
        when(buscarPorEmaiUseCase.execute("naoexiste@teste.com")).thenReturn(null);

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> loginGoogleAuthService.autenticar(request)
        );

        assertEquals("Usuario nao cadastrado", exception.getMessage());
        verify(generateTokenUseCase, never()).execute(any(Membro.class));
        verify(generateTokenUseCase, never()).getExpiresIn();
    }
}
