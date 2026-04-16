package com.diacono.diacono.domain.repository;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoConsolidadoDTO;
import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoEscaladoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEvento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EscalaEventoRepository {
    List<EscalaEventoConsolidadoDTO> findEscalaEventoConsolidadoByPeriodo(UUID idIgreja, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, EnumStatusEvento status, UUID ministerioId, String nomeEvento);
    List<EscalaEventoEscaladoDTO> findEscalaEventoEscaladoByEventoId(UUID idIgreja, UUID eventoId);
}
