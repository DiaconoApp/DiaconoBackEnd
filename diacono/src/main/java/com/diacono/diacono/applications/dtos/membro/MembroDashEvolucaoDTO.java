package com.diacono.diacono.applications.dtos.membro;

import java.time.LocalDate;

public record MembroDashEvolucaoDTO(
        LocalDate data,
        Long quantidade
) {


}