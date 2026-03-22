package com.diacono.diacono.presentation.dto.response;

import java.time.LocalDate;

public record MembroDashEvolucaoDTO(
        LocalDate data,
        Long quantidade
) {


}