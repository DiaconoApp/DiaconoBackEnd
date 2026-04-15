package com.diacono.diacono.applications.dtos.escalas;

import java.time.LocalDateTime;
import java.util.UUID;

public record EscalaEventoDTO(
        UUID idEventoExterno,
        String nomeReuniao,
        LocalDateTime dataHoraFim,
        LocalDateTime dataHoraInicio,
        int ministeriosEscalados,
        int ministeriosEscaladosConfirmados
) {
}
