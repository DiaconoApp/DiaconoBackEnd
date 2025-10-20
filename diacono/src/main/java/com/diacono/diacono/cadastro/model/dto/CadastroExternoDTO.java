package com.diacono.diacono.cadastro.model.dto;

import com.diacono.diacono.membro.model.dto.request.EnderecoMembroDTO;
import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CadastroExternoDTO(

        @NotNull(message = "Você deve estar associado a uma Igreja.")
        UUID fkIgreja,

        @NotBlank(message = "O campo nome não pode estar vazio.")
        String nome,

        @CPF(message = "O CPF informado é inválido.")
        String cpf,

        @PastOrPresent(message = "Data de nascimento inválida.")
        LocalDate dataNascimento,

        @Email(message = "Formato inválido para o email.")
        @NotBlank(message = "É necessário preencher o email.")
        String email,

        @Pattern(regexp = "^(1[1-9]|2[12478]|3([1-5]|[7-8])|4[1-9]|5(1|[3-5])|6[1-9]|7[134579]|8[1-9]|9[1-9])9[0-9]{8}$",
                message = "Formato inválido para o número de celular.")
        @NotBlank(message = "É necessário preencher o número de celular.")
        String celular,

        @NotBlank(message = "O campo senha não pode estar vazio")
        String senha,

        @Valid
        EnderecoMembroDTO membroEnderecoDTO
) {
    public CadastroExternoDTO {

    }
}
