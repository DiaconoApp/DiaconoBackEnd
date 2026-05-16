package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleAuthorizationCodeRequestDTO;
import com.diacono.diacono.applications.dtos.googleauth.GoogleIdTokenDTO;
import com.diacono.diacono.applications.dtos.googleauth.GoogleTokenResponseDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.infrastructure.auth.GoogleAuthorizationCodeExchanger;
import com.diacono.diacono.usecases.GenerateTokenUseCase;
import com.diacono.diacono.usecases.membro.BuscarPorEmaiUseCase;
import org.springframework.stereotype.Service;

@Service
public class LoginGoogleUseCase {

    private final GoogleAuthorizationCodeExchanger googleAuthorizationCodeExchanger;
    private final AutenticarGoogleUseCase autenticarGoogleUseCase;
    private final AtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase;
    private final GenerateTokenUseCase generateTokenUseCase;
    private final BuscarPorEmaiUseCase buscarPorEmaiUseCase;

    public LoginGoogleUseCase(GoogleAuthorizationCodeExchanger googleAuthorizationCodeExchanger,
                              AutenticarGoogleUseCase autenticarGoogleUseCase,
                              AtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase,
                              GenerateTokenUseCase generateTokenUseCase,
                              BuscarPorEmaiUseCase buscarPorEmaiUseCase) {
        this.googleAuthorizationCodeExchanger = googleAuthorizationCodeExchanger;
        this.autenticarGoogleUseCase = autenticarGoogleUseCase;
        this.atualizarSecretGoogleUseCase = atualizarSecretGoogleUseCase;
        this.generateTokenUseCase = generateTokenUseCase;
        this.buscarPorEmaiUseCase = buscarPorEmaiUseCase;
    }

    public LoginResponseDTO execute(GoogleAuthorizationCodeRequestDTO googleAuthRequestDTO) {
        GoogleTokenResponseDTO googleTokenResponse = googleAuthorizationCodeExchanger.exchange(googleAuthRequestDTO);
        GoogleIdTokenDTO googleClaims = autenticarGoogleUseCase.execute(googleTokenResponse.idToken());
        String email = googleClaims.email();

        Membro membro = buscarPorEmaiUseCase.execute(email);

        if (googleTokenResponse.refreshToken() != null && !googleTokenResponse.refreshToken().isBlank()) {
            atualizarSecretGoogleUseCase.execute(membro.getIdExterno(), email, googleTokenResponse.refreshToken());
        }

        return gerarLoginResponseDTO(membro);
    }

    private LoginResponseDTO gerarLoginResponseDTO(Membro membro) {
        String jwtValue = generateTokenUseCase.execute(membro);
        long expiresIn = generateTokenUseCase.getExpiresIn();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }
}
