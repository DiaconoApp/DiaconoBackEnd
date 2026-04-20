package com.diacono.diacono.infrastructure.persistence.dtos;

import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;

import java.time.LocalDateTime;
import java.util.UUID;

public record EscalaMinisterioQueryResult(
        UUID idExternoEscalaMinisterio,
        String nomeReuniao,
        String nomeMinisterio,
        LocalDateTime dataHoraFim,
        LocalDateTime dataHoraInicio,
        EnumStatusEscalaMinisterio status
) {
}
