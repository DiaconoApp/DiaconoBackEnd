package com.diacono.diacono.infrastructure.persistence.dtos;

import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;

import java.time.LocalDateTime;
import java.util.UUID;

public record EscalaMinisterioConsolidadoQueryResult(
        UUID idExternoEscalaEvento,
        String nomeReuniao,
        LocalDateTime dataHoraFim,
        LocalDateTime dataHoraInicio,
        int membrosEscalados,
        int membrosEscaladosConfirmados,
        EnumStatusEscalaMinisterio status
) {
}

