package com.diacono.diacono.ministerio.model.dto.response;

import com.diacono.diacono.ministerio.model.entity.EnumStatusMinisterio;

import java.time.LocalDate;
import java.util.UUID;

public record MinisterioSimplificadoDTO(UUID idExterno, String nome, String nomeLider ,EnumStatusMinisterio status, LocalDate dataCriacao) {
}
