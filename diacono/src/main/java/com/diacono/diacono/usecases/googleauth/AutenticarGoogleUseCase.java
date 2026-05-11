package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleIdTokenDTO;
import com.diacono.diacono.infrastructure.auth.GoogleIdTokenVerifier;
import com.diacono.diacono.global.config.GoogleOAuthProperties;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutenticarGoogleUseCase {

    private static final Logger logger = LoggerFactory.getLogger(AutenticarGoogleUseCase.class);
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

        logger.info("Google OAuth token validado com sucesso - email verificado");
        return googleClaims;
    }

    private void validarAudience(GoogleIdTokenDTO googleClaims) {
        List<String> audience = googleClaims.audience();
        String googleClientId = googleOAuthProperties.clientId();

        if (googleClientId == null || googleClientId.isBlank()) {
            logger.warn("Falha na autenticacao Google: Configuracao do Google OAuth ausente");
            throw new BadCredentialsException("Configuracao do Google OAuth ausente na aplicacao");
        }

        if (audience == null || !audience.contains(googleClientId)) {
            logger.warn("Falha na autenticacao Google: Token audience invalido - esperado clientId configurado");
            throw new BadCredentialsException("Token do Google nao pertence a aplicacao");
        }
    }

    private void validarEmailVerificado(GoogleIdTokenDTO googleClaims) {
        Boolean emailVerificado = googleClaims.emailVerified();
        String email = googleClaims.email();

        if (email == null || email.isBlank() || !Boolean.TRUE.equals(emailVerificado)) {
            logger.warn("Falha na autenticacao Google: Email nao verificado ou ausente");
            throw new BadCredentialsException("Email do Google nao verificado");
        }
    }
}

