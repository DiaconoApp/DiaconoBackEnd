package com.diacono.diacono.infrastructure.persistence.projection;

import java.util.UUID;

public record EscalaMembroMinisterioSimplificadoQueryResult(
        UUID idExternoMembroMinisterio,
        String nomeMembro
) {
}
