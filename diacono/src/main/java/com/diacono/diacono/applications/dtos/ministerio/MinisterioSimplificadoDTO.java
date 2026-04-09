package com.diacono.diacono.applications.dtos.ministerio;

import com.diacono.diacono.domain.enums.EnumStatusMinisterio;

import java.time.LocalDate;
import java.util.UUID;

public record MinisterioSimplificadoDTO(UUID idExterno, String nome, String nomeLider ,EnumStatusMinisterio status, LocalDate dataCriacao) {
}
