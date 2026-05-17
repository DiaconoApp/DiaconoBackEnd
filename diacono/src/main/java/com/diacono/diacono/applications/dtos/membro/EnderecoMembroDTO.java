package com.diacono.diacono.applications.dtos.membro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Locale;

public record EnderecoMembroDTO(

        @Pattern(regexp = "\\d{8}", message = "O cep deve conter exatamente 8 dígitos numéricos")
        @NotBlank(message = "O cep do endereço não pode ser nulo")
        String cep,

        @Size(min = 2, max = 50, message = "Estado deve conter entre {min} e {max} caracteres")
        @NotBlank(message = "O estado do endereço nulo")
        String estado,

        @Size(min = 2, max = 50, message = "Cidade deve conter entre {min} e {max} caracteres")
        @NotBlank(message = "A cidade do endereço não pode ser nulo")
        String cidade,

        @Size(min = 2, max = 50, message = "Bairro deve conter entre {min} e {max} caracteres")
        @NotBlank(message = "O bairro do endereço não pode ser nulo")
        String bairro,

        @Size(min = 2, max = 50, message = "Rua deve conter entre {min} e {max} caracteres")
        @NotBlank(message = "A rua do endereço não pode ser nulo")
        String rua,

        @Size(max = 100, message = "Complemento pode ter no máximo {max} caracteres")
        String complemento,

        @NotBlank(message = "O número da casa não pode ser nulo")
        String numero
) {

    public EnderecoMembroDTO {
        // Defensive normalization: trim and uppercase where appropriate, avoid NPE when optional fields are null
        cep = cep.strip();
        estado = estado.toUpperCase(Locale.ROOT);
        cidade = cidade.toUpperCase(Locale.ROOT);
        bairro = bairro.toUpperCase(Locale.ROOT);
        rua = rua.toUpperCase(Locale.ROOT);
        complemento = complemento.toUpperCase(Locale.ROOT);
        numero = numero.strip();
    }
}
