package com.diacono.diacono.membros.model.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record EnderecoMembroDTO(
        Integer id,

        @Pattern(regexp = "\\d{5}-\\d{3}")
        String cep,

        @Size(min = 2, max = 50, message = "Estado deve conter entre 2 e 100 caracteres")
        String estado,

        @Size(min = 2, max = 50, message = "Cidade deve conter entre 2 e 100 caracteres")
        String cidade,

        @Size(min = 2, max = 50, message = "Bairro deve conter entre 2 e 100 caracteres")
        String bairro,

        @Size(min = 2, max = 50, message = "Rua deve conter entre 2 e 100 caracteres")
        String rua,

        @Positive
        Integer numero,

        @Size(max = 100, message = "Complemento pode ter no máximo 100 caracteres")
        String complemento
) {}
