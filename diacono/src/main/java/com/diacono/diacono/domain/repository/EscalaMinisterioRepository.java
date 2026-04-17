package com.diacono.diacono.domain.repository;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioConsolidadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EscalaMinisterioRepository {
    List<EscalaMinisterioConsolidadoDTO> findEscalaMinisterioConsolidadoByPeriodo(UUID igrejaId, LocalDateTime inicioMes, LocalDateTime fimMes, EnumStatusEscalaMinisterio status, List<UUID> listaMinisterios, String nomeEvento);
}
