package com.diacono.diacono.presentation.dto.response;

import com.diacono.diacono.presentation.dto.MinisterioKpisResponseDTO;

public record KpisMinisteriosDTO(
        EventoKpiDTO eventoKpiDTO,
        MinisterioKpisResponseDTO ministerioKpisResponseDTO
) {
}
