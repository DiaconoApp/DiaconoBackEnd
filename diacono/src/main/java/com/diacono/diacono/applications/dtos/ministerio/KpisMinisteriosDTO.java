package com.diacono.diacono.applications.dtos.ministerio;

import com.diacono.diacono.applications.dtos.evento.EventoKpiDTO;

public record KpisMinisteriosDTO(
        EventoKpiDTO eventoKpiDTO,
        MinisterioKpisResponseDTO ministerioKpisResponseDTO
) {
}
