package com.diacono.diacono.applications.dtos.evento;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventoCriadoMessageDTO(
        UUID idExternoEvento,
        String nome,
        String descricao,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        UUID idExternoIgreja,
        UUID idExternoOrganizador
) {
}

