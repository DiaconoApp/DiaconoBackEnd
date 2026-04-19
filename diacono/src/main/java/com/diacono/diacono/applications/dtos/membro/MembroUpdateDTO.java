package com.diacono.diacono.applications.dtos.membro;

import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumGeneroMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public record MembroUpdateDTO(

        UUID fkIgreja,

        String nome,

        @CPF(message = "O CPF informado é inválido.")
        String cpf,

        LocalDate dataNascimento,

        @Email
        String email,

        @Pattern(regexp = "^(1[1-9]|2[12478]|3([1-5]|[7-8])|4[1-9]|5(1|[3-5])|6[1-9]|7[134579]|8[1-9]|9[1-9])9[0-9]{8}$")
        String celular,

        List<UUID> idExternoMinisterios,

        EnumCargoMembro cargo,

        EnumGeneroMembro generoMembro,

        @Valid
        EnderecoMembroDTO membroEnderecoDTO,

        EnumStatusMembro status
) {
}
