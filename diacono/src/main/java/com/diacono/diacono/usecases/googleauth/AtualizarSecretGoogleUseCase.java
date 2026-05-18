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
        LocalDateTime now = LocalDateTime.now();
        GoogleRefreshTokenMembro existingToken = validarEscopoIgreja(membroId, igrejaId).orElse(null);

        GoogleRefreshTokenMembro googleRefreshTokenMembro = existingToken == null
                ? GoogleRefreshTokenMembro.builder()
                    .membroId(membroId)
                    .igrejaId(igrejaId)
                    .email(email)
                    .refreshToken(refreshToken)
                    .createdAt(now)
                    .updatedAt(now)
                    .build()
                : existingToken.toBuilder()
                    .email(email)
                    .refreshToken(refreshToken)
                    .updatedAt(now)
                    .build();

        googleRefreshTokenMembroRepository.save(googleRefreshTokenMembro);
    }

    private java.util.Optional<GoogleRefreshTokenMembro> validarEscopoIgreja(UUID membroId, UUID igrejaId) {
        java.util.Optional<GoogleRefreshTokenMembro> tokenExistente = googleRefreshTokenMembroRepository.findByMembroId(membroId);

        tokenExistente
                .filter(token -> !igrejaId.equals(token.getIgrejaId()))
                .ifPresent(token -> {
                    logger.warn("Tentativa de atualizar refresh token fora do escopo da igreja. membroId=[{}], igrejaSolicitante=[{}], igrejaRegistro=[{}]",
                            membroId, igrejaId, token.getIgrejaId());
                    throw new BadCredentialsException("Usuario nao autorizado para atualizar token Google");
                });

        return tokenExistente;
    }
}
