package com.diacono.diacono.membro.model.dto;

import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;


public record MembrosUpdateDTO(

        String nome,

        String cpf,

        LocalDate dataNascimento,

        @Email
        String email,

        @Pattern(regexp = "^(1[1-9]|2[12478]|3([1-5]|[7-8])|4[1-9]|5(1|[3-5])|6[1-9]|7[134579]|8[1-9]|9[1-9])9[0-9]{8}$")
        String celular,

        List<MinisterioMembroCreateDTO> ministerio,

        EnumCargoMembro cargo,

        EnderecoMembroDTO membroEnderecoDTO
) {
}
