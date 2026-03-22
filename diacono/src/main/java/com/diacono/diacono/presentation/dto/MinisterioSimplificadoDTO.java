package com.diacono.diacono.presentation.dto;

import com.diacono.diacono.domain.enums.EnumStatusMinisterio;

import java.time.LocalDate;
import java.util.UUID;

public record MinisterioSimplificadoDTO(UUID idExterno, String nome, String nomeLider ,EnumStatusMinisterio status, LocalDate dataCriacao) {
}
