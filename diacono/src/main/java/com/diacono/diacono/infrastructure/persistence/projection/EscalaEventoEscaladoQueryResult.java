package com.diacono.diacono.infrastructure.persistence.projection;

import java.util.UUID;

public record EscalaEventoEscaladoQueryResult(
        UUID idExternoMinisterio,
        String nomeMinisterio,
        UUID idExternoEscalaEvento,
        Boolean isMinisterioEscalado
) {
}

