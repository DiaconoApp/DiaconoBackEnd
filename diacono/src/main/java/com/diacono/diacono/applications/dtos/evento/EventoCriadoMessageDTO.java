package com.diacono.diacono.applications.dtos.evento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventoCriadoMessageDTO(
        UUID idExternoEvento,
        String nome,
        String descricao,
        BigDecimal custo, // Adicionado agora
        String PublicoAlvo, // Adicionado agora
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        String Localizacao, // Adicionado agora
        UUID idExternoIgreja,
        UUID idExternoOrganizador
) {
}
