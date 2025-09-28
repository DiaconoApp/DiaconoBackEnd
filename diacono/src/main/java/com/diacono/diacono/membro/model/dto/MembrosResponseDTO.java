package com.diacono.diacono.membro.model.dto;

import java.time.LocalDate;

public record MembrosResponseDTO(String nome, LocalDate dataNascimento, Integer id, String email) {}
