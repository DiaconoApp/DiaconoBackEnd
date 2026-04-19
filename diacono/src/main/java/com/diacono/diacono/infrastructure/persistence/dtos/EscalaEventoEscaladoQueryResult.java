package com.diacono.diacono.infrastructure.persistence.dtos;

import java.util.UUID;

public record EscalaEventoEscaladoQueryResult(
        UUID idExternoMinisterio,
        String nomeMinisterio,
        UUID idExternoEscalaEvento,
        Boolean isMinisterioEscalado
) {
}

