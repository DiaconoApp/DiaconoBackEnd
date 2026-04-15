package com.diacono.diacono.domain.repository;

import com.diacono.diacono.applications.dtos.escalas.EscalaEventoDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EscalaEventoRepository {
    List<EscalaEventoDTO> findEscalaEventoByPeriodo(UUID idIgreja, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim);
}
