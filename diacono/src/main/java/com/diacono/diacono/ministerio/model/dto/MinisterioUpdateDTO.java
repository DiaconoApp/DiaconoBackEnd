package com.diacono.diacono.ministerio.model.dto;

import com.diacono.diacono.ministerio.model.entity.EnumStatusMinisterio;

import java.util.UUID;

public record MinisterioUpdateDTO(String nome, EnumStatusMinisterio status, UUID idLider) {
}
