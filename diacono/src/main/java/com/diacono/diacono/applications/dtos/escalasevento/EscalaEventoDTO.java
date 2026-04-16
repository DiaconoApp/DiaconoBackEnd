package com.diacono.diacono.applications.dtos.escalasevento;

import java.util.UUID;

public record EscalaEventoDTO(
        UUID idExternoEscalaEvento,
        UUID idExternoMinisterio,
        String nomeMinisterio

) {
}
