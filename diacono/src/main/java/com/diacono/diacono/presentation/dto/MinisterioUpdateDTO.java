package com.diacono.diacono.presentation.dto;

import com.diacono.diacono.domain.enums.EnumStatusMinisterio;

import java.util.UUID;

public record MinisterioUpdateDTO(String nome, EnumStatusMinisterio status, UUID idLider) {
}
