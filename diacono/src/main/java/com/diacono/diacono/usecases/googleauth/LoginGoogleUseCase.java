package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleAuthRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.applications.dtos.googleauth.GoogleIdTokenDTO;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.usecases.GenerateTokenUseCase;
import com.diacono.diacono.usecases.membro.BuscarPorEmaiUseCase;
import org.springframework.stereotype.Service;

@Service
public class LoginGoogleUseCase {

    private final AutenticarGoogleUseCase autenticarGoogleUseCase;
    private final AtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase;
    private final GenerateTokenUseCase generateTokenUseCase;
    private final BuscarPorEmaiUseCase buscarPorEmaiUseCase;

    public LoginGoogleUseCase(AutenticarGoogleUseCase autenticarGoogleUseCase,
                              AtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase,
                              GenerateTokenUseCase generateTokenUseCase,
                              BuscarPorEmaiUseCase buscarPorEmaiUseCase) {
        this.autenticarGoogleUseCase = autenticarGoogleUseCase;
        this.atualizarSecretGoogleUseCase = atualizarSecretGoogleUseCase;
        this.generateTokenUseCase = generateTokenUseCase;
        this.buscarPorEmaiUseCase = buscarPorEmaiUseCase;
    }

    public LoginResponseDTO execute(GoogleAuthRequestDTO googleAuthRequestDTO) {
        GoogleIdTokenDTO googleClaims = autenticarGoogleUseCase.execute(googleAuthRequestDTO.idToken());
        String email = googleClaims.email();

        Membro membro = buscarMembro(email);

        if (googleAuthRequestDTO.refreshToken() != null) {
            atualizarSecretGoogleUseCase.execute(
                membro.getIdExterno(),
                membro.getIgreja().getIdExterno(),
                email,
                googleAuthRequestDTO.refreshToken()
            );
        }

        return gerarLoginResponseDTO(membro);
    }

    private Membro buscarMembro(String email) {
        Membro membro = buscarPorEmaiUseCase.execute(email);
        if (membro == null) {
            throw new BadCredentialsException("Usuario nao cadastrado");
        }
        return membro;
    }

    private LoginResponseDTO gerarLoginResponseDTO(Membro membro) {
        String jwtValue = generateTokenUseCase.execute(membro);
        long expiresIn = generateTokenUseCase.getExpiresIn();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }
}
