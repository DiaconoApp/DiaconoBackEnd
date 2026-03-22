package com.diacono.diacono.presentation.dto;

import java.time.LocalDate;
import java.util.UUID;

public record MinisterioResponseDTO(UUID idExterno, String nome, LocalDate dataCriacao, String nomeLider, String status) {
}
