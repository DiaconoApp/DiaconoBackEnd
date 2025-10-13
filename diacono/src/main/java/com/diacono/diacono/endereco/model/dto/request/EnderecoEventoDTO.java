package com.diacono.diacono.endereco.model.dto.request;

import java.util.UUID;

public record EnderecoEventoDTO(

        //fazer as validações
        UUID idExterno,
        String cep,
        String rua,
        String cidade,
        String bairro,
        String complemento,
        String numero,
        String apelido
) {
}
