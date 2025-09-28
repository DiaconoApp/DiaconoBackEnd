package com.diacono.diacono.ministerio.model.dto;

import java.time.LocalDate;

public record MinisterioResponseDTO(String nome, LocalDate dataCriacao, String nomeLider, String status) {
}
