package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleAuthRequestDTO;
import com.diacono.diacono.applications.dtos.googleauth.GoogleIdTokenDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.usecases.GenerateTokenUseCase;
import com.diacono.diacono.usecases.membro.BuscarPorEmaiUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginGoogleUseCaseTest {

    @Mock
    private AutenticarGoogleUseCase autenticarGoogleUseCase;

    @Mock
    private AtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase;

    @Mock
    private GenerateTokenUseCase generateTokenUseCase;

    @Mock
    private BuscarPorEmaiUseCase buscarPorEmaiUseCase;

    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;

    @Captor
    private ArgumentCaptor<String> emailCaptor;

    @Captor
    private ArgumentCaptor<String> refreshTokenCaptor;

    private LoginGoogleUseCase loginGoogleUseCase;

    @BeforeEach
    void setup() {
        loginGoogleUseCase = new LoginGoogleUseCase(
                autenticarGoogleUseCase,
                atualizarSecretGoogleUseCase,
                generateTokenUseCase,
                buscarPorEmaiUseCase
        );
    }

    @Test
    void deveLogarQuandoTokenGoogleForValidoEMembroExistir() {
        String email = "usuario@teste.com";
        String refreshToken = "refresh-token-123";

        GoogleAuthRequestDTO request = new GoogleAuthRequestDTO("id-token", refreshToken);

        Membro membro = new Membro();

        when(autenticarGoogleUseCase.execute("id-token")).thenReturn(
            new GoogleIdTokenDTO(
                java.util.List.of("google-client-id"),
                email,
                true
            )
        );
        when(buscarPorEmaiUseCase.execute(email)).thenReturn(membro);
        when(generateTokenUseCase.execute(membro)).thenReturn("jwt-token");
        when(generateTokenUseCase.getExpiresIn()).thenReturn(3600L);

        LoginResponseDTO response = loginGoogleUseCase.execute(request);

        assertEquals("jwt-token", response.acessToken());
        assertEquals(3600L, response.expiresIn());

        // Captura os argumentos da chamada para validar
        verify(atualizarSecretGoogleUseCase).execute(
            uuidCaptor.capture(),
            emailCaptor.capture(),
            refreshTokenCaptor.capture()
        );
        assertEquals(email, emailCaptor.getValue());
        assertEquals(refreshToken, refreshTokenCaptor.getValue());
    }

    @Test
    void deveLancarExcecaoQuandoMembroNaoExistir() {
        GoogleAuthRequestDTO request = new GoogleAuthRequestDTO("id-token", "refresh-token");

        when(autenticarGoogleUseCase.execute("id-token")).thenReturn(
            new GoogleIdTokenDTO(
                java.util.List.of("google-client-id"),
                "naoexiste@teste.com",
                true
            )
        );
        when(buscarPorEmaiUseCase.execute("naoexiste@teste.com")).thenReturn(null);

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> loginGoogleUseCase.execute(request)
        );

        assertEquals("Usuario nao cadastrado", exception.getMessage());
        verify(atualizarSecretGoogleUseCase, never()).execute(any(), anyString(), anyString());
    }

    @Test
    void naoDevePersistirSecretSeRefreshTokenNaoForFornecido() {
        String email = "usuario@teste.com";

        GoogleAuthRequestDTO request = new GoogleAuthRequestDTO("id-token", null);

        Membro membro = new Membro();

        when(autenticarGoogleUseCase.execute("id-token")).thenReturn(
            new GoogleIdTokenDTO(
                java.util.List.of("google-client-id"),
                email,
                true
            )
        );
        when(buscarPorEmaiUseCase.execute(email)).thenReturn(membro);
        when(generateTokenUseCase.execute(membro)).thenReturn("jwt-token");
        when(generateTokenUseCase.getExpiresIn()).thenReturn(3600L);

        LoginResponseDTO response = loginGoogleUseCase.execute(request);

        assertEquals("jwt-token", response.acessToken());
        verify(atualizarSecretGoogleUseCase, never()).execute(any(), anyString(), anyString());
    }
}
