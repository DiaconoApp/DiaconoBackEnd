package com.diacono.diacono.applications.dtos.evento;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventoComEventoMinisterioDTO(
        UUID idExterno,
        String nome,
        LocalDateTime dataHoraFim,
        LocalDateTime dataHoraInicio,
        Long totalEventoMinisterios,
        Long totalEventosMinisterioConfirmados
) {
}
