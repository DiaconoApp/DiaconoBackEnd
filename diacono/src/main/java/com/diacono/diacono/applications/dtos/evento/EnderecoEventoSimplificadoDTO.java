package com.diacono.diacono.applications.dtos.evento;

import java.util.UUID;

public record EnderecoEventoSimplificadoDTO(
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
