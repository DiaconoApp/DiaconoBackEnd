package com.diacono.diacono.applications.dtos.membro;

public record EnderecoMembroResponseDTO(
        String cep,
        String estado,
        String cidade,
        String bairro,
        String rua,
        String complemento,
        Integer numero
) {}
