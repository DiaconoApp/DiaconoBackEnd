package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.domain.repository.GoogleRefreshTokenMembroRepository;
import com.diacono.diacono.domain.entity.GoogleRefreshTokenMembro;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AtualizarSecretGoogleUseCase {

    private final GoogleRefreshTokenMembroRepository googleRefreshTokenMembroRepository;

    public AtualizarSecretGoogleUseCase(GoogleRefreshTokenMembroRepository googleRefreshTokenMembroRepository) {
        this.googleRefreshTokenMembroRepository = googleRefreshTokenMembroRepository;
    }

    public void execute(UUID membroId, String email, String refreshToken) {
        GoogleRefreshTokenMembro googleRefreshTokenMembro = googleRefreshTokenMembroRepository.findByMembroId(membroId)
                .orElseGet(() -> GoogleRefreshTokenMembro.builder()
                        .membroId(membroId)
                        .build());

        googleRefreshTokenMembro.setEmail(email);
        googleRefreshTokenMembro.setTokenRefresh(refreshToken);

        googleRefreshTokenMembroRepository.save(googleRefreshTokenMembro);
    }
}
