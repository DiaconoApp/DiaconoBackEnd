package com.diacono.diacono.global.model.dto.response;

import java.util.UUID;

public record EnderecoSimplificadoDTO(
        String cep,
        String rua,
        String cidade,
        String bairro,
        String complemento,
        String numero,
        String apelido,
        UUID idExterno
) {
}
