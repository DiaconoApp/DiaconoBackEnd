package com.diacono.diacono.endereco.model.dto.request;

public record EnderecoDTO(

        //fazer as validações

        String cep,
        String rua,
        String cidade,
        String bairro,
        String complemento,
        String numero,
        String apelido
) {
}
