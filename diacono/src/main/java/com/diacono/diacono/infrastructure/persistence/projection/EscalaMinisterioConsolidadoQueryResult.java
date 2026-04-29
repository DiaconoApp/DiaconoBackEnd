package com.diacono.diacono.infrastructure.persistence.projection;

import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;

import java.time.LocalDateTime;
import java.util.UUID;

public record EscalaMinisterioConsolidadoQueryResult(
        UUID idEvento,
        UUID idExternoEscalaEvento,
        String nomeReuniao,
        String nomeMinisterio,
        LocalDateTime dataHoraFim,
        LocalDateTime dataHoraInicio,
        int membrosEscalados,
        int membrosEscaladosConfirmados,
        EnumStatusEscalaMinisterio status
) {
}

