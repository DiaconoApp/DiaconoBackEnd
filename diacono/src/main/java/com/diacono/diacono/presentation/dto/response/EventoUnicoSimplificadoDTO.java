package com.diacono.diacono.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventoUnicoSimplificadoDTO(
        UUID idExterno,
        String nome,
        LocalDateTime dataHoraFim,
        LocalDateTime dataHoraInicio
) {
}
