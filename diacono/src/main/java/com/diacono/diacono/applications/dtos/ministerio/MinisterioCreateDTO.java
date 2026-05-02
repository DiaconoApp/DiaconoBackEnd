package com.diacono.diacono.applications.dtos.ministerio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record MinisterioCreateDTO(
        @NotNull(message = "É obrigatório informar o id do líder do membro")
        UUID idLider,

        @NotBlank(message = "É obrigatório informar o nome do ministério")
        @Size(max = 100, message = "O nome do ministério deve ter no máximo {max} caracteres")
        String nome
) {
}