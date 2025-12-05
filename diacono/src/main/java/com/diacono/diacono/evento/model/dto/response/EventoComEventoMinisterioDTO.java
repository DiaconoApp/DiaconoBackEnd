package com.diacono.diacono.evento.model.dto.response;

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
