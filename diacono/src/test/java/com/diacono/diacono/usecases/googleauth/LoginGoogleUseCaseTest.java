package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleIdTokenDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.global.error.exceptions.GoogleAuthorizationCodeException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.GenerateTokenUseCase;
import com.diacono.diacono.usecases.membro.BuscarPorEmaiUseCase;
import org.junit.jupiter.api.Test;

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

        Object exchangerProxy = createExchangerProxy(exchangerClass, exchangerArgs, tokenResponse, null);

        UUID membroId = UUID.randomUUID();
        Membro membro = membroComIdExterno(membroId);

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
        assertEquals(email, atualizarSecretGoogleUseCase.receivedEmail);
        assertEquals(refreshToken, atualizarSecretGoogleUseCase.receivedRefreshToken);
    }

    @Test
    void naoDevePersistirRefreshTokenQuandoGoogleNaoOEnviar() throws Exception {
        Class<?> requestDtoClass = loadRequiredClass(GOOGLE_CODE_REQUEST_DTO);
        Class<?> tokenResponseDtoClass = loadRequiredClass(GOOGLE_TOKEN_RESPONSE_DTO);
        Class<?> exchangerClass = loadRequiredClass(GOOGLE_AUTHORIZATION_CODE_EXCHANGER);

        String email = "usuario@teste.com";
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
                mapWithNullableValues(
                        "accessToken", "google-access-token",
                        "idToken", idToken,
                        "refreshToken", null,
                        "expiresIn", 3600L,
                        "tokenType", "Bearer",
                        "scope", "openid email profile"
                )
        );

        Object exchangerProxy = createExchangerProxy(exchangerClass, new AtomicReference<>(), tokenResponse, null);
        Membro membro = membroComIdExterno(UUID.randomUUID());

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

        GoogleAuthorizationCodeException exchangeFailure =
                new GoogleAuthorizationCodeException("Authorization code invalido ou expirado");

        Object request = newRecordInstance(
                requestDtoClass,
                mapWithNullableValues(
                        "authorizationCode", "invalid-code",
                        "redirectUri", "https://app.exemplo.com/auth/google/callback",
                        "codeVerifier", null
                )
        );

        Object exchangerProxy = createExchangerProxy(exchangerClass, new AtomicReference<>(), null, exchangeFailure);
        FakeAutenticarGoogleUseCase autenticarGoogleUseCase = new FakeAutenticarGoogleUseCase(null);
        FakeAtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase = new FakeAtualizarSecretGoogleUseCase();
        FakeGenerateTokenUseCase generateTokenUseCase = new FakeGenerateTokenUseCase("jwt-token", 3600L);
        FakeBuscarPorEmaiUseCase buscarPorEmaiUseCase = new FakeBuscarPorEmaiUseCase((Membro) null);

        LoginGoogleUseCase loginGoogleUseCase = instantiateUseCase(
                exchangerClass,
                exchangerProxy,
                autenticarGoogleUseCase,
                atualizarSecretGoogleUseCase,
                generateTokenUseCase,
                buscarPorEmaiUseCase
        );

        GoogleAuthorizationCodeException thrown = assertThrows(
                GoogleAuthorizationCodeException.class,
                () -> executeUseCase(loginGoogleUseCase, request)
        );

        assertEquals("Authorization code invalido ou expirado", thrown.getMessage());
        assertEquals(null, autenticarGoogleUseCase.receivedIdToken);
        assertEquals(null, buscarPorEmaiUseCase.receivedEmail);
        assertEquals(null, atualizarSecretGoogleUseCase.receivedRefreshToken);
        assertEquals(null, generateTokenUseCase.receivedMembro);
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

        LoginGoogleUseCase loginGoogleUseCase = instantiateUseCase(
                exchangerClass,
                exchangerProxy,
                autenticarGoogleUseCase,
                atualizarSecretGoogleUseCase,
                generateTokenUseCase,
                buscarPorEmaiUseCase
        );

        ObjectNotFoundException thrown = assertThrows(
                ObjectNotFoundException.class,
                () -> executeUseCase(loginGoogleUseCase, request)
        );

        assertEquals("Membro não encontrado com o email fornecido.", thrown.getMessage());
        assertEquals(email, buscarPorEmaiUseCase.receivedEmail);
        assertEquals(null, atualizarSecretGoogleUseCase.receivedRefreshToken);
        assertEquals(null, generateTokenUseCase.receivedMembro);
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

        Object[] arguments = Arrays.stream(constructor.getParameterTypes())
                .map(parameterType -> resolveConstructorArgument(
                        parameterType,
                        exchangerClass,
                        exchangerProxy,
                        autenticarGoogleUseCase,
                        atualizarSecretGoogleUseCase,
                        generateTokenUseCase,
                        buscarPorEmaiUseCase
                ))
                .toArray();

        try {
            return (LoginGoogleUseCase) constructor.newInstance(arguments);
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("Nao foi possivel instanciar LoginGoogleUseCase para o novo fluxo.", exception);
        }
    }

    private Object resolveConstructorArgument(
            Class<?> parameterType,
            Class<?> exchangerClass,
            Object exchangerProxy,
            AutenticarGoogleUseCase autenticarGoogleUseCase,
            AtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase,
            GenerateTokenUseCase generateTokenUseCase,
            BuscarPorEmaiUseCase buscarPorEmaiUseCase
    ) {
        String parameterTypeName = parameterType.getName();

        if (parameterTypeName.equals(exchangerClass.getName())) {
            return exchangerProxy;
        }
        if (parameterType == AutenticarGoogleUseCase.class) {
            return autenticarGoogleUseCase;
        }
        if (parameterType == AtualizarSecretGoogleUseCase.class) {
            return atualizarSecretGoogleUseCase;
        }
        if (parameterType == GenerateTokenUseCase.class) {
            return generateTokenUseCase;
        }
        if (parameterType == BuscarPorEmaiUseCase.class) {
            return buscarPorEmaiUseCase;
        }

        throw new AssertionError("Dependencia inesperada no construtor de LoginGoogleUseCase: " + parameterTypeName);
    }

    private Object createExchangerProxy(
            Class<?> exchangerClass,
            AtomicReference<Object[]> exchangerArgs,
            Object response,
            RuntimeException failure
    ) {
        InvocationHandler handler = (proxy, method, args) -> {
            if (method.getDeclaringClass() == Object.class) {
                return switch (method.getName()) {
                    case "toString" -> "GoogleAuthorizationCodeExchangerProxy";
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "equals" -> proxy == args[0];
                    default -> null;
                };
            }

            exchangerArgs.set(args);

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

            return (LoginResponseDTO) executeMethod.invoke(useCase, request);
        } catch (ReflectiveOperationException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new AssertionError("Nao foi possivel executar LoginGoogleUseCase no novo fluxo.", exception);
        }
    }

    private Class<?> loadRequiredClass(String className) throws Exception {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException exception) {
            throw new AssertionError("Classe esperada para o novo fluxo ainda nao existe: " + className, exception);
        }
    }

    private Object newRecordInstance(Class<?> recordClass, Map<String, Object> valuesByComponentName) {
        if (!recordClass.isRecord()) {
            fail("A classe esperada deve ser um record: " + recordClass.getName());
        }

        try {
            var components = recordClass.getRecordComponents();
            Class<?>[] parameterTypes = Arrays.stream(components)
                    .map(component -> component.getType())
                    .toArray(Class<?>[]::new);

            Object[] args = Arrays.stream(components)
                    .map(component -> valuesByComponentName.get(component.getName()))
                    .toArray();

            Constructor<?> constructor = recordClass.getDeclaredConstructor(parameterTypes);
            return constructor.newInstance(args);
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("Nao foi possivel criar instancia de " + recordClass.getName(), exception);
        }
    }

    private Object extractArgumentValue(Object[] args, String expectedComponentName) {
        if (args == null || args.length == 0) {
            fail("A chamada ao trocador de authorization code deveria receber parametros.");
        }

        for (Object arg : args) {
            if (arg == null) {
                continue;
            }

            Class<?> argumentClass = arg.getClass();
            if (!argumentClass.isRecord()) {
                continue;
            }

            try {
                for (var component : argumentClass.getRecordComponents()) {
                    if (component.getName().equals(expectedComponentName)) {
                        Method accessor = component.getAccessor();
                        return accessor.invoke(arg);
                    }
                }
            } catch (ReflectiveOperationException exception) {
                throw new AssertionError("Falha ao inspecionar argumento repassado ao trocador do Google.", exception);
            }
        }

        fail("Nenhum argumento com componente '" + expectedComponentName + "' foi repassado ao trocador do Google.");
        return new HashMap<>();
    }

    private Map<String, Object> mapWithNullableValues(Object... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new AssertionError("Os valores do mapa de teste devem vir em pares chave/valor.");
        }

        Map<String, Object> values = new HashMap<>();
        for (int index = 0; index < keyValues.length; index += 2) {
            values.put((String) keyValues[index], keyValues[index + 1]);
        }
        return values;
    }

    private Membro membroComIdExterno(UUID idExterno) {
        Membro membro = new Membro();
        setField(membro, "idExterno", idExterno);
        return membro;
    }

    private void setField(Object target, String fieldName, Object value) {
        Class<?> current = target.getClass();

        while (current != null) {
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException exception) {
                current = current.getSuperclass();
            } catch (ReflectiveOperationException exception) {
                throw new AssertionError("Nao foi possivel configurar o campo '" + fieldName + "' no teste.", exception);
            }
        }

        throw new AssertionError("Campo nao encontrado para configuracao no teste: " + fieldName);
    }

    private static final class FakeAutenticarGoogleUseCase extends AutenticarGoogleUseCase {
        private final GoogleIdTokenDTO response;
        private String receivedIdToken;

        private FakeAutenticarGoogleUseCase(GoogleIdTokenDTO response) {
            super(null, null);
            this.response = response;
        }

        @Override
        public GoogleIdTokenDTO execute(String idToken) {
            receivedIdToken = idToken;
            return response;
        }
    }

    private static final class FakeAtualizarSecretGoogleUseCase extends AtualizarSecretGoogleUseCase {
        private UUID receivedMembroId;
        private String receivedEmail;
        private String receivedRefreshToken;

        private FakeAtualizarSecretGoogleUseCase() {
            super(null);
        }

        @Override
        public void execute(UUID membroId, String email, String refreshToken) {
            this.receivedMembroId = membroId;
            this.receivedEmail = email;
            this.receivedRefreshToken = refreshToken;
        }
    }

    private static final class FakeGenerateTokenUseCase extends GenerateTokenUseCase {
        private final String token;
        private final long expiresIn;
        private Membro receivedMembro;

        private FakeGenerateTokenUseCase(String token, long expiresIn) {
            super(null);
            this.token = token;
            this.expiresIn = expiresIn;
        }

        @Override
        public String execute(Membro membro) {
            this.receivedMembro = membro;
            return token;
        }

        @Override
        public long getExpiresIn() {
            return expiresIn;
        }
    }

    private static final class FakeBuscarPorEmaiUseCase extends BuscarPorEmaiUseCase {
        private final Membro membro;
        private final RuntimeException failure;
        private String receivedEmail;

        private FakeBuscarPorEmaiUseCase(Membro membro) {
            super(null);
            this.membro = membro;
            this.failure = null;
        }

        private FakeBuscarPorEmaiUseCase(RuntimeException failure) {
            super(null);
            this.membro = null;
            this.failure = failure;
        }

        @Override
        public Membro execute(String email) {
            this.receivedEmail = email;
            if (failure != null) {
                throw failure;
            }
            return membro;
        }
    }
}
