package com.diacono.diacono.dashboard.model.response.ministerio;

import com.diacono.diacono.evento.model.dto.response.EventoKpiDTO;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioKpisResponseDTO;

public record KpisMinisteriosDTO(
        EventoKpiDTO eventoKpiDTO,
        MinisterioKpisResponseDTO ministerioKpisResponseDTO
) {
}
