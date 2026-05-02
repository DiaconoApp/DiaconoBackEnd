package com.diacono.diacono.applications.dtos.membro;

import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record MembroResponseDTO(
        UUID idExterno,
        String nome,
        String email,
        String celular,
        LocalDate dataNascimento,
        Set<MembroMinisterioDTO> ministerios,
        EnumStatusMembro status,
        EnumCargoMembro cargo
) {
}
