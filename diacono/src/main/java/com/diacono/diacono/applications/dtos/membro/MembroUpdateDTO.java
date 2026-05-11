package com.diacono.diacono.applications.dtos.membro;

import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumGeneroMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public record MembroUpdateDTO(

        UUID fkIgreja,

        @Size(min = 1, max = 255, message = "O nome deve ter entre {min} e {max} caracteres.")
        String nome,

        @CPF(message = "O CPF informado é inválido.")
        @Size(min = 11, max = 14, message = "O CPF deve ter entre {min} e {max} caracteres.")
        String cpf,

        LocalDate dataNascimento,

        @Email(message = "Formato inválido para o email.")
        @Size(max = 255, message = "O email deve ter no máximo {max} caracteres.")
        String email,

        @Pattern(
                regexp = "^(1[1-9]|2[12478]|3([1-5]|[7-8])|4[1-9]|5(1|[3-5])|6[1-9]|7[134579]|8[1-9]|9[1-9])9[0-9]{8}$",
                message = "O celular deve seguir o formato válido, exemplo: 11987654321."
        )
        @Size(min = 10, max = 11, message = "O celular deve ter entre {min} e {max} dígitos.")
        String celular,

        @Size(max = 100, message = "O número de ministérios não pode exceder {max}.")
        List<UUID> idExternoMinisterios,

        EnumCargoMembro cargo,

        EnumGeneroMembro generoMembro,

        @Valid
        EnderecoMembroDTO membroEnderecoDTO,

        EnumStatusMembro status
) {
}
