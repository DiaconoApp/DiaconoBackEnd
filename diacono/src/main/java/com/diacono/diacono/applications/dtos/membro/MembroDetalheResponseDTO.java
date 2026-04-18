package com.diacono.diacono.applications.dtos.membro;

import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumGeneroMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record MembroDetalheResponseDTO(
        UUID idExterno,
        UUID fkIgreja,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        String email,
        String celular,
        UUID idExternoMinisterios,
        EnumCargoMembro cargo,
        EnumGeneroMembro generoMembro,
        EnderecoMembroDTO membroEnderecoDTO,
        Set<MembroMinisterioDTO> ministerios,
        EnumStatusMembro status
) {
}

