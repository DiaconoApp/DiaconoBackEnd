package com.diacono.diacono.applications.dtos.membro;

import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;

import java.time.LocalDate;
import java.util.UUID;

public record MembroMinisterioInfoMembroDTO (UUID idExternoMembro, String nome, String email, String numeroCelular, EnumStatusMembro status, LocalDate dataNascimento, EnumCargoMembroMinisterio cargo, String nomeMinisterio){
}
