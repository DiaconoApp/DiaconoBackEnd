package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleAuthorizationCodeRequestDTO;
import com.diacono.diacono.applications.dtos.googleauth.GoogleIdTokenDTO;
import com.diacono.diacono.applications.dtos.googleauth.GoogleTokenResponseDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.global.error.exceptions.GoogleAuthorizationCodeException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.auth.GoogleAuthorizationCodeExchanger;
import com.diacono.diacono.usecases.GenerateTokenUseCase;
import com.diacono.diacono.usecases.membro.BuscarPorEmaiUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginGoogleUseCaseTest {

    @Test
    void deveLogarQuandoAuthorizationCodeForTrocadoComSucessoERetornarRefreshToken() {
        String email = "usuario@teste.com";
        String refreshToken = "refresh-token-123";
        String idToken = "google-id-token";
        UUID membroId = UUID.randomUUID();
        UUID igrejaId = UUID.randomUUID();

        GoogleAuthorizationCodeRequestDTO request =
                new GoogleAuthorizationCodeRequestDTO("google-auth-code", "http://localhost:5173/auth/google/callback", "pkce-code-verifier");

        Membro membro = membroComIds(membroId, igrejaId);

        FakeAtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase = new FakeAtualizarSecretGoogleUseCase();
        LoginGoogleUseCase loginGoogleUseCase = new LoginGoogleUseCase(
                fakeExchanger(new GoogleTokenResponseDTO("google-access-token", idToken, refreshToken, 3600L, "Bearer", "openid email profile")),
                fakeAutenticador(new GoogleIdTokenDTO(java.util.List.of("google-client-id"), email, true)),
                atualizarSecretGoogleUseCase,
                fakeGenerateTokenUseCase("jwt-token", 3600L),
                fakeBuscarPorEmailUseCase(membro)
        );

        LoginResponseDTO response = loginGoogleUseCase.execute(request);

        assertEquals("jwt-token", response.acessToken());
        assertEquals(3600L, response.expiresIn());
        assertEquals(membroId, atualizarSecretGoogleUseCase.receivedMembroId);
        assertEquals(igrejaId, atualizarSecretGoogleUseCase.receivedIgrejaId);
        assertEquals(email, atualizarSecretGoogleUseCase.receivedEmail);
        assertEquals(refreshToken, atualizarSecretGoogleUseCase.receivedRefreshToken);
    }

    @Test
    void naoDevePersistirRefreshTokenQuandoGoogleNaoOEnviar() {
        String email = "usuario@teste.com";
        String idToken = "google-id-token";
        UUID membroId = UUID.randomUUID();
        UUID igrejaId = UUID.randomUUID();

        GoogleAuthorizationCodeRequestDTO request =
                new GoogleAuthorizationCodeRequestDTO("google-auth-code", "http://localhost:5173/auth/google/callback", null);

        Membro membro = membroComIds(membroId, igrejaId);

        FakeAtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase = new FakeAtualizarSecretGoogleUseCase();
        LoginGoogleUseCase loginGoogleUseCase = new LoginGoogleUseCase(
                fakeExchanger(new GoogleTokenResponseDTO("google-access-token", idToken, null, 3600L, "Bearer", "openid email profile")),
                fakeAutenticador(new GoogleIdTokenDTO(java.util.List.of("google-client-id"), email, true)),
                atualizarSecretGoogleUseCase,
                fakeGenerateTokenUseCase("jwt-token", 3600L),
                fakeBuscarPorEmailUseCase(membro)
        );

        LoginResponseDTO response = loginGoogleUseCase.execute(request);

        assertEquals("jwt-token", response.acessToken());
        assertEquals(null, atualizarSecretGoogleUseCase.receivedRefreshToken);
    }

    @Test
    void devePropagarFalhaNaTrocaDoAuthorizationCodeESemAvancarNoFluxo() {
        GoogleAuthorizationCodeRequestDTO request =
                new GoogleAuthorizationCodeRequestDTO("invalid-code", "http://localhost:5173/auth/google/callback", null);

        LoginGoogleUseCase loginGoogleUseCase = new LoginGoogleUseCase(
                ignored -> { throw new GoogleAuthorizationCodeException("Authorization code invalido ou expirado"); },
                fakeAutenticador(null),
                new FakeAtualizarSecretGoogleUseCase(),
                fakeGenerateTokenUseCase("jwt-token", 3600L),
                fakeBuscarPorEmailUseCase(null)
        );

        GoogleAuthorizationCodeException exception = assertThrows(
                GoogleAuthorizationCodeException.class,
                () -> loginGoogleUseCase.execute(request)
        );

        assertEquals("Authorization code invalido ou expirado", exception.getMessage());
    }

    @Test
    void devePropagarQuandoEmailDoGoogleNaoCorresponderAMembroInterno() {
        String email = "naoexiste@teste.com";
        String idToken = "google-id-token";

        GoogleAuthorizationCodeRequestDTO request =
                new GoogleAuthorizationCodeRequestDTO("google-auth-code", "http://localhost:5173/auth/google/callback", null);

        LoginGoogleUseCase loginGoogleUseCase = new LoginGoogleUseCase(
                fakeExchanger(new GoogleTokenResponseDTO("google-access-token", idToken, "refresh-token", 3600L, "Bearer", "openid email profile")),
                fakeAutenticador(new GoogleIdTokenDTO(java.util.List.of("google-client-id"), email, true)),
                new FakeAtualizarSecretGoogleUseCase(),
                fakeGenerateTokenUseCase("jwt-token", 3600L),
                fakeBuscarPorEmailUseCase(new ObjectNotFoundException("Membro não encontrado com o email fornecido."))
        );

        ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                () -> loginGoogleUseCase.execute(request)
        );

        assertEquals("Membro não encontrado com o email fornecido.", exception.getMessage());
    }

    private GoogleAuthorizationCodeExchanger fakeExchanger(GoogleTokenResponseDTO response) {
        return ignored -> response;
    }

    private AutenticarGoogleUseCase fakeAutenticador(GoogleIdTokenDTO response) {
        return new AutenticarGoogleUseCase(null, null) {
            @Override
            public GoogleIdTokenDTO execute(String idToken) {
                return response;
            }
        };
    }

    private GenerateTokenUseCase fakeGenerateTokenUseCase(String token, long expiresIn) {
        return new GenerateTokenUseCase(null) {
            @Override
            public String execute(Membro membro) {
                return token;
            }

            @Override
            public long getExpiresIn() {
                return expiresIn;
            }
        };
    }

    private BuscarPorEmaiUseCase fakeBuscarPorEmailUseCase(Membro membro) {
        return new BuscarPorEmaiUseCase(null) {
            @Override
            public Membro execute(String email) {
                return membro;
            }
        };
    }

    private BuscarPorEmaiUseCase fakeBuscarPorEmailUseCase(ObjectNotFoundException exception) {
        return new BuscarPorEmaiUseCase(null) {
            @Override
            public Membro execute(String email) {
                throw exception;
            }
        };
    }

    private Membro membroComIds(UUID membroId, UUID igrejaId) {
        Membro membro = new Membro();
        Igreja igreja = new Igreja();
        ReflectionTestUtils.setField(membro, "idExterno", membroId);
        ReflectionTestUtils.setField(igreja, "idExterno", igrejaId);
        membro.setIgreja(igreja);
        return membro;
    }

    private static final class FakeAtualizarSecretGoogleUseCase extends AtualizarSecretGoogleUseCase {
        private UUID receivedMembroId;
        private UUID receivedIgrejaId;
        private String receivedEmail;
        private String receivedRefreshToken;

        private FakeAtualizarSecretGoogleUseCase() {
            super(null);
        }

        @Override
        public void execute(UUID membroId, UUID igrejaId, String email, String refreshToken) {
            this.receivedMembroId = membroId;
            this.receivedIgrejaId = igrejaId;
            this.receivedEmail = email;
            this.receivedRefreshToken = refreshToken;
        }
    }
}
