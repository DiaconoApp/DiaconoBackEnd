package com.diacono.diacono.membro.model.dto.response;

import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record MembroResponseDTO(
        UUID idExterno,
        String nome,
        String email,
        String celular,
        LocalDate dataNascimento,
        Set<MembroMinisterio> ministerios,
        EnumStatusMembro status
        ) {}
