package com.diacono.diacono.applications.dtos.escalaministerio;

import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;

import java.time.LocalDateTime;
import java.util.UUID;

public record EscalaMinisterioConsolidadoDTO(
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
