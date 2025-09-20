package com.diacono.diacono.ministerios.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record MinisterioResponseDTO(String nome, LocalDate dataCriacao, String nomeLider, String status) {
}
