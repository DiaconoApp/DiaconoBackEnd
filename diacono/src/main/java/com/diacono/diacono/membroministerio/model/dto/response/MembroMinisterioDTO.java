package com.diacono.diacono.membroministerio.model.dto.response;

import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membroministerio.model.entity.EnumCargoMembroMinisterio;

import java.time.LocalDate;
import java.util.UUID;

public record MembroMinisterioDTO(
        UUID idExterno, String nome, String email, String telefone, EnumStatusMembro status, LocalDate dataNascimento, EnumCargoMembroMinisterio cargo, String nomeMinisterio
) {
}
