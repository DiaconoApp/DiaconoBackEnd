package com.diacono.diacono.membros.model.dto;

import java.time.LocalDate;

public record MembrosResponseDTO(
        Integer id,
        String nome,
        String email,
        String celular,
        LocalDate dataNascimento,
        Boolean ativo
        ) {}
