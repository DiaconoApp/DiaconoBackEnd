package com.diacono.diacono.eventoministerio.model.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventoMinisterioEscalaDTO(
        UUID idExterno,
        String nome,
        LocalDateTime dataHoraFim,
        LocalDateTime dataHoraInicio,
        int membrosEscalados
) {
}
