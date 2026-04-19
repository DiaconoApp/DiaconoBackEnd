package com.diacono.diacono.domain.repository;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoConsolidadoDTO;
import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoEscaladoDTO;
import com.diacono.diacono.domain.entity.EscalaEvento;
import com.diacono.diacono.domain.enums.EnumStatusEvento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EscalaEventoRepository {
    List<EscalaEventoConsolidadoDTO> findEscalaEventoConsolidadoByPeriodo(UUID idIgreja, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, EnumStatusEvento status, UUID ministerioId, String nomeEvento);
    List<EscalaEventoEscaladoDTO> findEscalaEventoEscaladoByEventoId(UUID idIgreja, UUID eventoId);
    UUID findMinisterioIdByEscalaEventoId(UUID idIgreja, UUID escalaEventoId);
    EscalaEvento findEscalaEventoByIdExternoAndIgrejaId(UUID igrejaId, UUID escalaEventoId);
    UUID findEventoIdByEscalaEventoId(UUID escalaEventoId);
    boolean areAllConfirmadosByEventoId(UUID eventoId);
    void updateStatusByEscalaEventoId(UUID escalaEventoId, EnumStatusEvento status);
}
