package com.diacono.diacono.applications.dtos.evento;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventoUnicoSimplificadoDTO(
        UUID idExterno,
        String nome,
        LocalDateTime dataHoraFim,
        LocalDateTime dataHoraInicio
) {
}
