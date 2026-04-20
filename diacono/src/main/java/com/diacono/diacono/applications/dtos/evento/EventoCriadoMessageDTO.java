package com.diacono.diacono.applications.dtos.evento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventoCriadoMessageDTO(
        UUID idEvento,
        String nome,
        String descricao,
        BigDecimal custo,
        String publicoAlvo,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        String localizacao,
        UUID idIgreja
) {
}
