package com.diacono.diacono.evento.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Locale;
import java.util.UUID;

public record EnderecoEventoDTO(

        //fazer as validações
        UUID idExterno,

        @Pattern(regexp = "\\d{5}\\d{3}")
        String cep,

        String estado,

        String cidade,

        String bairro,

        String rua,

        String complemento,

        String numero,

        String apelido
) {
    public EnderecoEventoDTO {
        estado = estado.toUpperCase(Locale.ROOT);
        cidade = cidade.toUpperCase(Locale.ROOT);
        bairro = bairro.toUpperCase(Locale.ROOT);
        rua = rua.toUpperCase(Locale.ROOT);
        complemento = complemento.toUpperCase(Locale.ROOT);
    }
}
