package com.diacono.diacono.applications.dtos.evento;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Locale;
import java.util.UUID;

public record EnderecoEventoDTO(
        // TODO: Padronizar o uso do toUpperCaseOrNull

        UUID idExterno,

        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos numéricos.")
        String cep,

        @Size(max = 255, message = "O estado deve conter no máximo {max} caracteres.")
        String estado,

        @Size(max = 255, message = "A cidade deve conter no máximo {max} caracteres.")
        String cidade,

        @Size(max = 255, message = "O bairro deve conter no máximo {max} caracteres.")
        String bairro,

        @Size(max = 255, message = "A rua deve conter no máximo {max} caracteres.")
        String rua,

        @Size(max = 255, message = "O complemento deve conter no máximo {max} caracteres.")
        String complemento,

        @Size(max = 20, message = "O número deve conter no máximo {max} caracteres.")
        String numero,

        @Size(max = 255, message = "O apelido deve conter no máximo {max} caracteres.")
        String apelido
) {
    public EnderecoEventoDTO {
        estado = toUpperCaseOrNull(estado);
        cidade = toUpperCaseOrNull(cidade);
        bairro = toUpperCaseOrNull(bairro);
        rua = toUpperCaseOrNull(rua);
        complemento = toUpperCaseOrNull(complemento);
    }

    private static String toUpperCaseOrNull(String value) {
        return value == null ? null : value.toUpperCase(Locale.ROOT);
    }
}
