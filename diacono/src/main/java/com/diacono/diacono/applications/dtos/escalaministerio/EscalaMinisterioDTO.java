package com.diacono.diacono.applications.dtos.escalaministerio;

import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;

import java.time.LocalDateTime;
import java.util.UUID;

public record EscalaMinisterioDTO(
        UUID idExternoEscalaMinisterio,
        String nomeReuniao,
        String nomeMinisterio,
        LocalDateTime dataHoraFim,
        LocalDateTime dataHoraInicio,
        EnumStatusEscalaMinisterio status
) {
}
