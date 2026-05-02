package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.domain.repository.GoogleRefreshTokenMembroRepository;
import com.diacono.diacono.domain.entity.GoogleRefreshTokenMembro;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AtualizarSecretGoogleUseCase {

    private final GoogleRefreshTokenMembroRepository googleRefreshTokenMembroRepository;

    public AtualizarSecretGoogleUseCase(GoogleRefreshTokenMembroRepository googleRefreshTokenMembroRepository) {
        this.googleRefreshTokenMembroRepository = googleRefreshTokenMembroRepository;
    }

    public void execute(UUID membroId, UUID igrejaId, String email, String refreshToken) {
        LocalDateTime now = LocalDateTime.now();

        GoogleRefreshTokenMembro googleRefreshTokenMembro = GoogleRefreshTokenMembro.builder()
                .membroId(membroId)
                .igrejaId(igrejaId)
                .email(email)
                .refreshToken(refreshToken)
                .createdAt(now)
                .updatedAt(now)
                .build();

        googleRefreshTokenMembroRepository.save(googleRefreshTokenMembro);
    }
}
