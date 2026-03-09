package com.diacono.diacono.applications.dtos.ministerio;

import java.time.LocalDate;
import java.util.UUID;

public record MinisterioResponseDTO(UUID idExterno, String nome, LocalDate dataCriacao, String nomeLider, String status) {
}
