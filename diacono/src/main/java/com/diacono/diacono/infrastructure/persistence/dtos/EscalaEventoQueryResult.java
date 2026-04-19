package com.diacono.diacono.infrastructure.persistence.dtos;

import com.diacono.diacono.domain.enums.EnumStatusEvento;

import java.time.LocalDateTime;
import java.util.UUID;

public record EscalaEventoQueryResult(
        UUID idEventoExterno,
        String nomeReuniao,
        LocalDateTime dataHoraFim,
        LocalDateTime dataHoraInicio,
        int ministeriosEscalados,
        int ministeriosEscaladosConfirmados,
        EnumStatusEvento status
) {
}

