package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleIdTokenDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.global.error.exceptions.GoogleAuthorizationCodeException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.GenerateTokenUseCase;
import com.diacono.diacono.usecases.membro.BuscarPorEmaiUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
class LoginGoogleUseCaseTest {

    private static final String GOOGLE_CODE_REQUEST_DTO =
            "com.diacono.diacono.applications.dtos.googleauth.GoogleAuthorizationCodeRequestDTO";
    private static final String GOOGLE_TOKEN_RESPONSE_DTO =
            "com.diacono.diacono.applications.dtos.googleauth.GoogleTokenResponseDTO";
    private static final String GOOGLE_AUTHORIZATION_CODE_EXCHANGER =
            "com.diacono.diacono.infrastructure.auth.GoogleAuthorizationCodeExchanger";

    @Test
    void deveLogarQuandoAuthorizationCodeForTrocadoComSucessoERetornarRefreshToken() throws Exception {
        Class<?> requestDtoClass = loadRequiredClass(GOOGLE_CODE_REQUEST_DTO);
        Class<?> tokenResponseDtoClass = loadRequiredClass(GOOGLE_TOKEN_RESPONSE_DTO);
        Class<?> exchangerClass = loadRequiredClass(GOOGLE_AUTHORIZATION_CODE_EXCHANGER);
        AtomicReference<Object[]> exchangerArgs = new AtomicReference<>();

        String authorizationCode = "google-auth-code";
        String redirectUri = "https://app.exemplo.com/auth/google/callback";
        String codeVerifier = "pkce-code-verifier";
        String email = "usuario@teste.com";
        String idToken = "google-id-token";
        String refreshToken = "google-refresh-token";

        Object request = newRecordInstance(
                requestDtoClass,
                Map.of(
                        "authorizationCode", authorizationCode,
                        "redirectUri", redirectUri,
                        "codeVerifier", codeVerifier
                )
        );

        Object tokenResponse = newRecordInstance(
                tokenResponseDtoClass,
                Map.of(
                        "accessToken", "google-access-token",
                        "idToken", idToken,
                        "refreshToken", refreshToken,
                        "expiresIn", 3600L,
                        "tokenType", "Bearer",
                        "scope", "openid email profile"
                )
        );

    @Captor
    private ArgumentCaptor<UUID> igrejaIdCaptor;

    @Captor
    private ArgumentCaptor<String> emailCaptor;

        UUID membroId = UUID.randomUUID();
        UUID igrejaId = UUID.randomUUID();
        Membro membro = membroComIdExterno(membroId, igrejaId);

        FakeAutenticarGoogleUseCase autenticarGoogleUseCase = new FakeAutenticarGoogleUseCase(
                new GoogleIdTokenDTO(java.util.List.of("google-client-id"), email, true)
        );
        FakeAtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase = new FakeAtualizarSecretGoogleUseCase();
        FakeGenerateTokenUseCase generateTokenUseCase = new FakeGenerateTokenUseCase("jwt-token", 3600L);
        FakeBuscarPorEmaiUseCase buscarPorEmaiUseCase = new FakeBuscarPorEmaiUseCase(membro);

        LoginGoogleUseCase loginGoogleUseCase = instantiateUseCase(
                exchangerClass,
                exchangerProxy,
                autenticarGoogleUseCase,
                atualizarSecretGoogleUseCase,
                generateTokenUseCase,
                buscarPorEmaiUseCase
        );

        LoginResponseDTO response = executeUseCase(loginGoogleUseCase, request);

        assertEquals("jwt-token", response.acessToken());
        assertEquals(3600L, response.expiresIn());

        Object[] args = exchangerArgs.get();
        assertNotNull(args, "A troca do authorization code deveria ocorrer antes da autenticacao interna.");
        assertEquals(authorizationCode, extractArgumentValue(args, "authorizationCode"));
        assertEquals(redirectUri, extractArgumentValue(args, "redirectUri"));
        assertEquals(codeVerifier, extractArgumentValue(args, "codeVerifier"));

        assertEquals(idToken, autenticarGoogleUseCase.receivedIdToken);
        assertEquals(email, buscarPorEmaiUseCase.receivedEmail);
        assertEquals(membroId, atualizarSecretGoogleUseCase.receivedMembroId);
        assertEquals(igrejaId, atualizarSecretGoogleUseCase.receivedIgrejaId);
        assertEquals(email, atualizarSecretGoogleUseCase.receivedEmail);
        assertEquals(refreshToken, atualizarSecretGoogleUseCase.receivedRefreshToken);
    }

    @Test
    void naoDevePersistirRefreshTokenQuandoGoogleNaoOEnviar() throws Exception {
        Class<?> requestDtoClass = loadRequiredClass(GOOGLE_CODE_REQUEST_DTO);
        Class<?> tokenResponseDtoClass = loadRequiredClass(GOOGLE_TOKEN_RESPONSE_DTO);
        Class<?> exchangerClass = loadRequiredClass(GOOGLE_AUTHORIZATION_CODE_EXCHANGER);

        String email = "usuario@teste.com";
        String refreshToken = "refresh-token-123";
        UUID membroId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID igrejaId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        Object tokenResponse = newRecordInstance(
                tokenResponseDtoClass,
                mapWithNullableValues(
                        "accessToken", "google-access-token",
                        "idToken", idToken,
                        "refreshToken", null,
                        "expiresIn", 3600L,
                        "tokenType", "Bearer",
                        "scope", "openid email profile"
                )
        );

        Membro membro = new Membro();
        Igreja igreja = new Igreja();
        ReflectionTestUtils.setField(membro, "idExterno", membroId);
        ReflectionTestUtils.setField(igreja, "idExterno", igrejaId);
        membro.setIgreja(igreja);

        FakeAutenticarGoogleUseCase autenticarGoogleUseCase = new FakeAutenticarGoogleUseCase(
                new GoogleIdTokenDTO(java.util.List.of("google-client-id"), email, true)
        );
        FakeAtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase = new FakeAtualizarSecretGoogleUseCase();
        FakeGenerateTokenUseCase generateTokenUseCase = new FakeGenerateTokenUseCase("jwt-token", 3600L);
        FakeBuscarPorEmaiUseCase buscarPorEmaiUseCase = new FakeBuscarPorEmaiUseCase(membro);

        LoginGoogleUseCase loginGoogleUseCase = instantiateUseCase(
                exchangerClass,
                exchangerProxy,
                autenticarGoogleUseCase,
                atualizarSecretGoogleUseCase,
                generateTokenUseCase,
                buscarPorEmaiUseCase
        );

        LoginResponseDTO response = executeUseCase(loginGoogleUseCase, request);

        assertEquals("jwt-token", response.acessToken());
        assertEquals(null, atualizarSecretGoogleUseCase.receivedRefreshToken);
    }

    @Test
    void devePropagarFalhaNaTrocaDoAuthorizationCodeESemAvancarNoFluxo() throws Exception {
        Class<?> requestDtoClass = loadRequiredClass(GOOGLE_CODE_REQUEST_DTO);
        Class<?> exchangerClass = loadRequiredClass(GOOGLE_AUTHORIZATION_CODE_EXCHANGER);

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
    void devePropagarQuandoEmailDoGoogleNaoCorresponderAMembroInterno() throws Exception {
        Class<?> requestDtoClass = loadRequiredClass(GOOGLE_CODE_REQUEST_DTO);
        Class<?> tokenResponseDtoClass = loadRequiredClass(GOOGLE_TOKEN_RESPONSE_DTO);
        Class<?> exchangerClass = loadRequiredClass(GOOGLE_AUTHORIZATION_CODE_EXCHANGER);

        String email = "naoexiste@teste.com";
        String idToken = "google-id-token";

        Object request = newRecordInstance(
                requestDtoClass,
                mapWithNullableValues(
                        "authorizationCode", "google-auth-code",
                        "redirectUri", "https://app.exemplo.com/auth/google/callback",
                        "codeVerifier", null
                )
        );

        Object tokenResponse = newRecordInstance(
                tokenResponseDtoClass,
                Map.of(
                        "accessToken", "google-access-token",
                        "idToken", idToken,
                        "refreshToken", "google-refresh-token",
                        "expiresIn", 3600L,
                        "tokenType", "Bearer",
                        "scope", "openid email profile"
                )
        );

        Object exchangerProxy = createExchangerProxy(exchangerClass, new AtomicReference<>(), tokenResponse, null);
        FakeAutenticarGoogleUseCase autenticarGoogleUseCase = new FakeAutenticarGoogleUseCase(
                new GoogleIdTokenDTO(java.util.List.of("google-client-id"), email, true)
        );
        FakeAtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase = new FakeAtualizarSecretGoogleUseCase();
        FakeGenerateTokenUseCase generateTokenUseCase = new FakeGenerateTokenUseCase("jwt-token", 3600L);
        FakeBuscarPorEmaiUseCase buscarPorEmaiUseCase = new FakeBuscarPorEmaiUseCase(
                new ObjectNotFoundException("Membro não encontrado com o email fornecido.")
        );

        assertEquals("Usuario nao cadastrado", exception.getMessage());
        verify(atualizarSecretGoogleUseCase, never()).execute(any(), any(), anyString(), anyString());
    }

    private LoginGoogleUseCase instantiateUseCase(
            Class<?> exchangerClass,
            Object exchangerProxy,
            AutenticarGoogleUseCase autenticarGoogleUseCase,
            AtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase,
            GenerateTokenUseCase generateTokenUseCase,
            BuscarPorEmaiUseCase buscarPorEmaiUseCase
    ) {
        Constructor<?> constructor = Arrays.stream(LoginGoogleUseCase.class.getConstructors())
                .filter(candidate -> candidate.getParameterCount() == 5)
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "LoginGoogleUseCase deve expor um construtor com 5 dependencias, incluindo o trocador de authorization code."
                ));

        GoogleAuthRequestDTO request = new GoogleAuthRequestDTO("id-token", null);
        UUID igrejaId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        Membro membro = new Membro();
        Igreja igreja = new Igreja();
        ReflectionTestUtils.setField(igreja, "idExterno", igrejaId);
        membro.setIgreja(igreja);

            if (failure != null) {
                throw failure;
            }

            return response;
        };

        return Proxy.newProxyInstance(
                exchangerClass.getClassLoader(),
                new Class<?>[]{exchangerClass},
                handler
        );
    }

    private LoginResponseDTO executeUseCase(LoginGoogleUseCase useCase, Object request) {
        try {
            Method executeMethod = Arrays.stream(LoginGoogleUseCase.class.getMethods())
                    .filter(method -> method.getName().equals("execute"))
                    .filter(method -> method.getParameterCount() == 1)
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("LoginGoogleUseCase deve expor execute com um unico request DTO."));

        assertEquals("jwt-token", response.acessToken());
        verify(atualizarSecretGoogleUseCase, never()).execute(any(), any(), anyString(), anyString());
    }
}
