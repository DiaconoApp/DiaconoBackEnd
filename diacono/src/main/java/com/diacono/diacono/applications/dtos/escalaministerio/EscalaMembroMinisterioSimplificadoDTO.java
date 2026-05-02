package com.diacono.diacono.applications.dtos.escalaministerio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record EscalaMembroMinisterioSimplificadoDTO(
        @NotNull(message = "idExternoMembroMinisterio é obrigatório")
        UUID idExternoMembroMinisterio,

        @NotBlank(message = "nomeMembro é obrigatório")
        @Size(max = 255, message = "nomeMembro deve ter no máximo {max} caracteres")
        String nomeMembro
) {
}
