package com.diacono.diacono.membro.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Locale;
import java.util.UUID;

public record EnderecoMembroDTO(

        @Pattern(regexp = "\\d{5}\\d{3}")
        @NotBlank(message = "O cep do endereço não pode ser nulo")
        String cep,

        @Size(min = 2, max = 50, message = "Estado deve conter entre 2 e 100 caracteres")
        @NotBlank(message = "O estado do endereço nulo")
        String estado,

        @Size(min = 2, max = 50, message = "Cidade deve conter entre 2 e 100 caracteres")
        @NotBlank(message = "A cidade do endereço pode ser nulo")
        String cidade,

        @Size(min = 2, max = 50, message = "Bairro deve conter entre 2 e 100 caracteres")
        @NotBlank(message = "O bairro do endereço não pode ser nulo")
        String bairro,

        @Size(min = 2, max = 50, message = "Rua deve conter entre 2 e 100 caracteres")
        @NotBlank(message = "A rua do endereço não pode ser nulo")
        String rua,

        @Size(max = 100, message = "Complemento pode ter no máximo 100 caracteres")
        String complemento,

        @NotBlank(message = "O número da casa não pode ser nulo")
        String numero
) {

    public EnderecoMembroDTO {
        estado = estado.toUpperCase(Locale.ROOT);
        cidade = cidade.toUpperCase(Locale.ROOT);
        bairro = bairro.toUpperCase(Locale.ROOT);
        rua = rua.toUpperCase(Locale.ROOT);
        complemento = complemento.toUpperCase(Locale.ROOT);
    }
}



