package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleIdTokenDTO;
import com.diacono.diacono.infrastructure.auth.GoogleIdTokenVerifier;
import com.diacono.diacono.global.config.GoogleOAuthProperties;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutenticarGoogleUseCase {

    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final GoogleOAuthProperties googleOAuthProperties;

    public AutenticarGoogleUseCase(GoogleIdTokenVerifier googleIdTokenVerifier,
                                   GoogleOAuthProperties googleOAuthProperties) {
        this.googleIdTokenVerifier = googleIdTokenVerifier;
        this.googleOAuthProperties = googleOAuthProperties;
    }

    public GoogleIdTokenDTO execute(String idToken) {
        GoogleIdTokenDTO googleClaims = googleIdTokenVerifier.verify(idToken);

        validarAudience(googleClaims);
        validarEmailVerificado(googleClaims);

        return googleClaims;
    }

    private void validarAudience(GoogleIdTokenDTO googleClaims) {
        List<String> audience = googleClaims.audience();
        String googleClientId = googleOAuthProperties.clientId();

        if (googleClientId == null || googleClientId.isBlank()) {
            throw new BadCredentialsException("Configuracao do Google OAuth ausente na aplicacao");
        }

        if (audience == null || !audience.contains(googleClientId)) {
            throw new BadCredentialsException("Token do Google nao pertence a aplicacao");
        }
    }

    private void validarEmailVerificado(GoogleIdTokenDTO googleClaims) {
        Boolean emailVerificado = googleClaims.emailVerified();
        String email = googleClaims.email();

        if (email == null || email.isBlank() || !Boolean.TRUE.equals(emailVerificado)) {
            throw new BadCredentialsException("Email do Google nao verificado");
        }
    }
}

