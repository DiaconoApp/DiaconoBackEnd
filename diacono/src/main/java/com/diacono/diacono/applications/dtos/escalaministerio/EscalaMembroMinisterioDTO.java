package com.diacono.diacono.applications.dtos.escalaministerio;

import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;

import java.util.UUID;

public record EscalaMembroMinisterioDTO(
        UUID membroMinisterioId,
        String nomeMembro,
        EnumStatusEscalaMinisterio status,
        Boolean isMembroOcupado
) {
}
