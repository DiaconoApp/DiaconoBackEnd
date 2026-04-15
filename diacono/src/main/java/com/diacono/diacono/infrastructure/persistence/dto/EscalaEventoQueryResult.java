package com.diacono.diacono.infrastructure.persistence.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record EscalaEventoQueryResult(
        UUID idEventoExterno,
        String nomeReuniao,
        LocalDateTime dataHoraFim,
        LocalDateTime dataHoraInicio,
        int ministeriosEscalados,
        int ministeriosEscaladosConfirmados
) {
}

