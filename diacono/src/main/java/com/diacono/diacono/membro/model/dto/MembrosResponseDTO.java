package com.diacono.diacono.membro.model.dto;

import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.ministerio.model.entity.Ministerio;

import java.time.LocalDate;
import java.util.List;

public record MembrosResponseDTO(
        String nome,
        String email,
        String celular,
        LocalDate dataNascimento,
        List<Ministerio> ministerio,
        EnumStatusMembro statusMembro
        ) {}
