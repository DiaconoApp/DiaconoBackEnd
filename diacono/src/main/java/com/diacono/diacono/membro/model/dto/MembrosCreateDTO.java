package com.diacono.diacono.membro.model.dto;

import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.ministerio.model.dto.MinisterioCreateDTO;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.List;

public record MembrosCreateDTO(

        @NotBlank(message = "O campo nome não pode estar vazio")
        String nome,

        @CPF(message = "O CPF informado é inválido")
        String cpf,

        @PastOrPresent(message = "Data de nascimento inválida")
        LocalDate dataNascimento,

        @Email @NotBlank
        String email,

        @Pattern(regexp = "^(1[1-9]|2[12478]|3([1-5]|[7-8])|4[1-9]|5(1|[3-5])|6[1-9]|7[134579]|8[1-9]|9[1-9])9[0-9]{8}$")
        String celular,

        @NotBlank(message = "O campo senha não pode estar vazio")
        String senha,

        List<MinisterioMembroCreateDTO> ministerio,

        EnumCargoMembro cargo,

        EnderecoMembroDTO membroEnderecoDTO
) {}
