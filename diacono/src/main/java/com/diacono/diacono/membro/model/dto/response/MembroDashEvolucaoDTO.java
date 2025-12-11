package com.diacono.diacono.membro.model.dto.response;

import java.time.LocalDate;

public record MembroDashEvolucaoDTO(
        LocalDate data,
        Long quantidade
) {


}