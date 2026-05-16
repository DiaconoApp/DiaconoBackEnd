package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.domain.repository.GoogleRefreshTokenMembroRepository;
import com.diacono.diacono.domain.entity.GoogleRefreshTokenMembro;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AtualizarSecretGoogleUseCase {

    private static final Logger logger = LoggerFactory.getLogger(AtualizarSecretGoogleUseCase.class);

    private final GoogleRefreshTokenMembroRepository googleRefreshTokenMembroRepository;

    public AtualizarSecretGoogleUseCase(GoogleRefreshTokenMembroRepository googleRefreshTokenMembroRepository) {
        this.googleRefreshTokenMembroRepository = googleRefreshTokenMembroRepository;
    }

    public void execute(UUID membroId, UUID igrejaId, String email, String refreshToken) {
        validarEscopoIgreja(membroId, igrejaId);

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

    private void validarEscopoIgreja(UUID membroId, UUID igrejaId) {
        googleRefreshTokenMembroRepository.findByMembroId(membroId)
                .filter(token -> !igrejaId.equals(token.getIgrejaId()))
                .ifPresent(token -> {
                    logger.warn("Tentativa de atualizar refresh token fora do escopo da igreja. membroId=[{}], igrejaSolicitante=[{}], igrejaRegistro=[{}]",
                            membroId, igrejaId, token.getIgrejaId());
                    throw new BadCredentialsException("Usuario nao autorizado para atualizar token Google");
                });
    }
}
