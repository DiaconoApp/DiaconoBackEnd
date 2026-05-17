package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleIdTokenDTO;
import com.diacono.diacono.global.config.GoogleOAuthProperties;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.infrastructure.auth.GoogleIdTokenVerifier;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AutenticarGoogleUseCaseTest {

    @Test
    void deveRetornarClaimsQuandoAudienceContiverClientIdEEmailEstiverVerificado() {
        GoogleIdTokenDTO claims = new GoogleIdTokenDTO(
                List.of("google-client-id", "outra-audience"),
                "usuario@teste.com",
                true
        );

        FakeGoogleIdTokenVerifier googleIdTokenVerifier = new FakeGoogleIdTokenVerifier(claims);
        AutenticarGoogleUseCase autenticarGoogleUseCase = new AutenticarGoogleUseCase(
                googleIdTokenVerifier,
                new GoogleOAuthProperties("google-client-id", "google-client-secret", null)
        );

        GoogleIdTokenDTO response = autenticarGoogleUseCase.execute("id-token-google");

        assertSame(claims, response);
        assertEquals("id-token-google", googleIdTokenVerifier.receivedIdToken);
    }

    @Test
    void deveLancarExcecaoQuandoClientIdNaoEstiverConfigurado() {
        AutenticarGoogleUseCase autenticarGoogleUseCase = new AutenticarGoogleUseCase(
                new FakeGoogleIdTokenVerifier(new GoogleIdTokenDTO(List.of("google-client-id"), "usuario@teste.com", true)),
                new GoogleOAuthProperties("", "google-client-secret", null)
        );

        BadCredentialsException thrown = assertThrows(
                BadCredentialsException.class,
                () -> autenticarGoogleUseCase.execute("id-token-google")
        );

        assertEquals("Configuracao do Google OAuth ausente na aplicacao", thrown.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoAudienceNaoPertencerAAplicacao() {
        AutenticarGoogleUseCase autenticarGoogleUseCase = new AutenticarGoogleUseCase(
                new FakeGoogleIdTokenVerifier(new GoogleIdTokenDTO(List.of("client-id-de-outra-app"), "usuario@teste.com", true)),
                new GoogleOAuthProperties("google-client-id", "google-client-secret", null)
        );

        BadCredentialsException thrown = assertThrows(
                BadCredentialsException.class,
                () -> autenticarGoogleUseCase.execute("id-token-google")
        );

        assertEquals("Token do Google nao pertence a aplicacao", thrown.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoEmailNaoEstiverVerificado() {
        AutenticarGoogleUseCase autenticarGoogleUseCase = new AutenticarGoogleUseCase(
                new FakeGoogleIdTokenVerifier(new GoogleIdTokenDTO(List.of("google-client-id"), "usuario@teste.com", false)),
                new GoogleOAuthProperties("google-client-id", "google-client-secret", null)
        );

        BadCredentialsException thrown = assertThrows(
                BadCredentialsException.class,
                () -> autenticarGoogleUseCase.execute("id-token-google")
        );

        assertEquals("Email do Google nao verificado", thrown.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoEmailVierAusente() {
        AutenticarGoogleUseCase autenticarGoogleUseCase = new AutenticarGoogleUseCase(
                new FakeGoogleIdTokenVerifier(new GoogleIdTokenDTO(List.of("google-client-id"), "", true)),
                new GoogleOAuthProperties("google-client-id", "google-client-secret", null)
        );

        BadCredentialsException thrown = assertThrows(
                BadCredentialsException.class,
                () -> autenticarGoogleUseCase.execute("id-token-google")
        );

        assertEquals("Email do Google nao verificado", thrown.getMessage());
    }

    private static final class FakeGoogleIdTokenVerifier implements GoogleIdTokenVerifier {
        private final GoogleIdTokenDTO response;
        private String receivedIdToken;

        private FakeGoogleIdTokenVerifier(GoogleIdTokenDTO response) {
            this.response = response;
        }

        @Override
        public GoogleIdTokenDTO verify(String idToken) {
            this.receivedIdToken = idToken;
            return response;
        }
    }
}
