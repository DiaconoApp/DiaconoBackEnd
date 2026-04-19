package com.diacono.diacono.applications.dtos.escalasevento;

import java.util.UUID;

public record EscalaEventoEscaladoDTO(
        UUID idExternoMinisterio,
        String nomeMinisterio,
        UUID idExternoEscalaEvento,
        Boolean isMinisterioEscalado
) {
}
