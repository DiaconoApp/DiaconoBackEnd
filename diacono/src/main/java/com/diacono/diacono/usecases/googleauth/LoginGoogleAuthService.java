package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleAuthRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.domain.auth.GoogleIdTokenClaims;
import com.diacono.diacono.domain.auth.GoogleIdTokenVerifier;
import com.diacono.diacono.global.config.GoogleOAuthProperties;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.usecases.GenerateTokenUseCase;
import com.diacono.diacono.usecases.membro.BuscarPorEmaiUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginGoogleAuthService {

    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final GenerateTokenUseCase generateTokenUseCase;
    private final BuscarPorEmaiUseCase buscarPorEmaiUseCase;
    private final GoogleOAuthProperties googleOAuthProperties;

    public LoginGoogleAuthService(GoogleIdTokenVerifier googleIdTokenVerifier,
                                  GenerateTokenUseCase generateTokenUseCase,
                                  BuscarPorEmaiUseCase buscarPorEmaiUseCase,
                                  GoogleOAuthProperties googleOAuthProperties
    ) {
        this.googleIdTokenVerifier = googleIdTokenVerifier;
        this.generateTokenUseCase = generateTokenUseCase;
        this.buscarPorEmaiUseCase = buscarPorEmaiUseCase;
        this.googleOAuthProperties = googleOAuthProperties;
    }

    public LoginResponseDTO execute(GoogleAuthRequestDTO googleAuthRequestDTO) {
        GoogleIdTokenClaims googleClaims = googleIdTokenVerifier.verify(googleAuthRequestDTO.idToken());

        validarAudience(googleClaims);
        String email = buscarEmailValido(googleClaims);

        Membro membro = buscarPorEmaiUseCase.execute(email);
        if (membro == null) {
            throw new BadCredentialsException("Usuario nao cadastrado");
            //	membro = criarMembroPeloGoogle(googleJwt, email);
        }

        String jwtValue = generateTokenUseCase.execute(membro);
        long expiresIn = generateTokenUseCase.getExpiresIn();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }

    private void validarAudience(GoogleIdTokenClaims googleClaims) {
        List<String> audience = googleClaims.audience();
        String googleClientId = googleOAuthProperties.clientId();

        if (googleClientId == null || googleClientId.isBlank()) {
            throw new BadCredentialsException("Configuracao do Google OAuth ausente na aplicacao");
        }

        if (audience == null || !audience.contains(googleClientId)) {
            throw new BadCredentialsException("Token do Google nao pertence a aplicacao");
        }
    }

    private String buscarEmailValido(GoogleIdTokenClaims googleClaims) {
        Boolean emailVerificado = googleClaims.emailVerified();
        String email = googleClaims.email();

        if (email == null || email.isBlank() || !Boolean.TRUE.equals(emailVerificado)) {
            throw new BadCredentialsException("Email do Google nao verificado");
        }

        return email;
    }

}
