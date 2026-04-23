package com.diacono.diacono.applications.dtos.ministerio;

import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record MinisterioUpdateDTO(
        @Size(max = 100, message = "O nome do ministério deve ter no máximo 100 caracteres")
        String nome,
        EnumStatusMinisterio status,
        UUID idLider
) {
}
