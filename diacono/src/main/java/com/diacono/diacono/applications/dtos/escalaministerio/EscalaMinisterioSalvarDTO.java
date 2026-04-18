package com.diacono.diacono.applications.dtos.escalaministerio;

import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EscalaMinisterioSalvarDTO(
        @NotNull(message = "É obrigatório informar o id do membro do ministério")
        UUID idExternoMembroMinisterio,
        EnumStatusEscalaMinisterio status
) {
}

