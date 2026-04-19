package com.diacono.diacono.infrastructure.persistence.dtos;

import java.util.UUID;

public record EscalaMembroMinisterioSimplificadoQueryResult(
        UUID idExternoMembroMinisterio,
        String nomeMembro
) {
}
