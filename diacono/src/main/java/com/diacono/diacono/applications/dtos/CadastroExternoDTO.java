package com.diacono.diacono.applications.dtos;

import com.diacono.diacono.applications.dtos.membro.EnderecoMembroDTO;
import com.diacono.diacono.domain.enums.EnumGeneroMembro;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

public record CadastroExternoDTO(

        @NotNull(message = "Você deve estar associado a uma Igreja.")
        UUID fkIgreja,

        @NotBlank(message = "O campo nome não pode estar vazio.")
        @Size(min = 1, max = 255, message = "O nome deve ter entre {min} e {max} caracteres.")
        String nome,

        @CPF(message = "O CPF informado é inválido.")
        @Size(min = 11, max = 11, message = "O CPF deve conter exatamente 11 dígitos.")
        String cpf,

        @PastOrPresent(message = "Data de nascimento inválida.")
        LocalDate dataNascimento,

        @Email(message = "Formato inválido para o email.")
        @NotBlank(message = "É necessário preencher o email.")
        @Size(max = 255, message = "O email deve ter no máximo {max} caracteres.")
        String email,

        @Pattern(regexp = "^(1[1-9]|2[12478]|3([1-5]|[7-8])|4[1-9]|5(1|[3-5])|6[1-9]|7[134579]|8[1-9]|9[1-9])9[0-9]{8}$",
                message = "Formato inválido para o número de celular.")
        @NotBlank(message = "É necessário preencher o número de celular.")
        @Size(min = 11, max = 11, message = "O celular deve conter exatamente 11 dígitos.")
        String celular,

        @NotBlank(message = "O campo senha não pode estar vazio")
        @Size(min = 8, max = 128, message = "A senha deve ter entre {min} e {max} caracteres.")
        String senha,


        @NotNull(message = "O gênero do membro deve ser informado.")
        EnumGeneroMembro generoMembro,

        @Valid
        EnderecoMembroDTO membroEnderecoDTO
) {
    public CadastroExternoDTO {
        nome = nome.toUpperCase(Locale.ROOT);
        email = email.toLowerCase(Locale.ROOT);
    }
}
