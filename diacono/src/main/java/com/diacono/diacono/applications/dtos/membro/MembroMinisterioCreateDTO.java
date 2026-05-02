package com.diacono.diacono.applications.dtos.membro;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MembroMinisterioCreateDTO(
        @NotNull(message = "O Id do membro deve ser informado.")
        UUID idExterno
){
}
