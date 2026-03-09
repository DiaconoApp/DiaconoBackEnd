package com.diacono.diacono.applications.dtos.membro;

import com.diacono.diacono.domain.enums.EnumStatusMinisterio;

import java.util.UUID;

public record MembroMinisterioDTO(
        UUID idExternoMinisterio,  String nomeMinisterio, String nomeLider, EnumStatusMinisterio status
) {
}
