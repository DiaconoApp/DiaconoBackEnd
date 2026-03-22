package com.diacono.diacono.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MinisterioCreateDTO(
        @NotNull(message = "É obrigatório informar o id do líder do membro")
        UUID idLider,
        @NotBlank(message = "É obrigatório informar o nome do ministério")
        String nome
) {
}
