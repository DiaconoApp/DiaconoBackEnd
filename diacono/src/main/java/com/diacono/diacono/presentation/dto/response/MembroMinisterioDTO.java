package com.diacono.diacono.presentation.dto.response;

import com.diacono.diacono.domain.enums.EnumStatusMinisterio;

import java.util.UUID;

public record MembroMinisterioDTO(
        UUID idExternoMinisterio,  String nomeMinisterio, String nomeLider, EnumStatusMinisterio status
) {
}
