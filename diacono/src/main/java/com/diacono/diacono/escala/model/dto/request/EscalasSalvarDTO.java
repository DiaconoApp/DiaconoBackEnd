package com.diacono.diacono.escala.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record EscalasSalvarDTO(
        @NotNull
        @Size(min = 1, message = "As escalas devem ser preenchidas.")
        List<UUID> fkMembrosMinisterio
) {

}