package com.diacono.diacono.applications.dtos.ministerio;

import com.diacono.diacono.domain.enums.EnumStatusMinisterio;

import java.util.UUID;

public record MinisterioUpdateDTO(String nome, EnumStatusMinisterio status, UUID idLider) {
}
