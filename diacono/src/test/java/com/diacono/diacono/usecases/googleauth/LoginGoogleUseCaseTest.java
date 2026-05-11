package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleAuthRequestDTO;
import com.diacono.diacono.applications.dtos.googleauth.GoogleIdTokenDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.domain.entity.Igreja;
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
import org.springframework.test.util.ReflectionTestUtils;

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
    private ArgumentCaptor<UUID> igrejaIdCaptor;

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
        UUID membroId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID igrejaId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        GoogleAuthRequestDTO request = new GoogleAuthRequestDTO("id-token", refreshToken);

        Membro membro = new Membro();
        Igreja igreja = new Igreja();
        ReflectionTestUtils.setField(membro, "idExterno", membroId);
        ReflectionTestUtils.setField(igreja, "idExterno", igrejaId);
        membro.setIgreja(igreja);

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
            igrejaIdCaptor.capture(),
            emailCaptor.capture(),
            refreshTokenCaptor.capture()
        );
        assertEquals(membroId, uuidCaptor.getValue());
        assertEquals(igrejaId, igrejaIdCaptor.getValue());
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
        verify(atualizarSecretGoogleUseCase, never()).execute(any(), any(), anyString(), anyString());
    }

    @Test
    void naoDevePersistirSecretSeRefreshTokenNaoForFornecido() {
        String email = "usuario@teste.com";

        GoogleAuthRequestDTO request = new GoogleAuthRequestDTO("id-token", null);
        UUID igrejaId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        Membro membro = new Membro();
        Igreja igreja = new Igreja();
        ReflectionTestUtils.setField(igreja, "idExterno", igrejaId);
        membro.setIgreja(igreja);

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
        verify(atualizarSecretGoogleUseCase, never()).execute(any(), any(), anyString(), anyString());
    }
}
