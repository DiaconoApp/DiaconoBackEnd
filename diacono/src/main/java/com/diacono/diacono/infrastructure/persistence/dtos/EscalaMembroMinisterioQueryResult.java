package com.diacono.diacono.infrastructure.persistence.dtos;

import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;

import java.util.UUID;

public record EscalaMembroMinisterioQueryResult(
        UUID membroMinisterioId,
        String nomeMembro,
        EnumStatusEscalaMinisterio status,
        Boolean isMembroOcupado
) {
}
