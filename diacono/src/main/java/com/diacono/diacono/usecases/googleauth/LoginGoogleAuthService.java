package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleAuthRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.global.config.GoogleOAuthProperties;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.usecases.GenerateTokenUseCase;
import com.diacono.diacono.usecases.membro.BuscarPorEmaiUseCase;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginGoogleAuthService {

    private final JwtDecoder googleJwtDecoder;
    private final GenerateTokenUseCase generateTokenUseCase;
    private final BuscarPorEmaiUseCase buscarPorEmaiUseCase;
    private final GoogleOAuthProperties googleOAuthProperties;

    public LoginGoogleAuthService(GenerateTokenUseCase generateTokenUseCase,
                                  BuscarPorEmaiUseCase buscarPorEmaiUseCase,
                                  GoogleOAuthProperties googleOAuthProperties
    ) {
        this.googleJwtDecoder = JwtDecoders.fromOidcIssuerLocation("https://accounts.google.com");
        this.generateTokenUseCase = generateTokenUseCase;
        this.buscarPorEmaiUseCase = buscarPorEmaiUseCase;
        this.googleOAuthProperties = googleOAuthProperties;
    }

    public LoginResponseDTO autenticar(GoogleAuthRequestDTO googleAuthRequestDTO) {
        Jwt googleJwt = validarGoogleJwt(googleAuthRequestDTO.idToken());

        validarAudience(googleJwt);
        String email = buscarEmailValido(googleJwt);

        Membro membro = buscarPorEmaiUseCase.execute(email);
        if (membro == null) {
            throw new BadCredentialsException("Usuario nao cadastrado");
            //	membro = criarMembroPeloGoogle(googleJwt, email);
        }

        String jwtValue = generateTokenUseCase.execute(membro);
        long expiresIn = generateTokenUseCase.getExpiresIn();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }

    private Jwt validarGoogleJwt(String idToken) {
        try {
            return googleJwtDecoder.decode(idToken);
        } catch (JwtException exception) {
            throw new BadCredentialsException("Token do Google invalido");
        }
    }

    private void validarAudience(Jwt googleJwt) {
        List<String> audience = googleJwt.getAudience();
        String googleClientId = googleOAuthProperties.clientId();

        if (googleClientId == null || googleClientId.isBlank()) {
            throw new BadCredentialsException("Configuracao do Google OAuth ausente na aplicacao");
        }

        System.out.println("Audience do token do Google: " + audience);
        System.out.println("Client ID esperado: " + googleClientId);

        if (audience == null || !audience.contains(googleClientId)) {
            throw new BadCredentialsException("Token do Google nao pertence a aplicacao");
        }
    }

    private String buscarEmailValido(Jwt googleJwt) {
        Boolean emailVerificado = googleJwt.getClaim("email_verified");
        String email = googleJwt.getClaimAsString("email");

        if (email == null || email.isBlank() || !Boolean.TRUE.equals(emailVerificado)) {
            throw new BadCredentialsException("Email do Google nao verificado");
        }

        return email;
    }

}


