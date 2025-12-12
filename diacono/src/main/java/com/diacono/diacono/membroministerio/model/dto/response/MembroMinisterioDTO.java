package com.diacono.diacono.membroministerio.model.dto.response;

import com.diacono.diacono.ministerio.model.entity.EnumStatusMinisterio;

import java.util.UUID;

public record MembroMinisterioDTO(
        UUID idExternoMinisterio,  String nomeMinisterio, String nomeLider, EnumStatusMinisterio status
) {
}
