package com.diacono.diacono.ministerios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record MinisterioCreateDTO(@NotBlank String nome, @PastOrPresent @NotNull LocalDate dataCriacao, @NotBlank String nomeLider, @NotBlank String status) {
}
