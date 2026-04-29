package com.diacono.diacono.domain.repository;

import com.diacono.diacono.applications.dtos.evento.EventoKpiDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioEventoDashDTO;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.entity.Recorrencia;
import com.diacono.diacono.domain.enums.EnumStatusEvento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventoRepository {
    List<Evento> findByPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim, UUID igrejaFk);
    Optional<Evento> findByIdExterno(UUID idExterno);
    long deleteByIdExterno(UUID idExterno);
    List<Evento> findByPeriodoAndRecorrencia(Recorrencia recorrencia, LocalDateTime dataInicio, UUID igrejaFk);

    Evento save(Evento evento);
    List<Evento> saveAll(List<Evento> eventos);
    void deleteAll(List<Evento> eventos);
    void updateStatusByEventoId(UUID eventoId, EnumStatusEvento status);

    List<EventoKpiDTO> buscarKpisEvento(int anoInicio, int anoFim, UUID idIgreja);
    List<MinisterioEventoDashDTO> contarEventosPorMinisterioNoPeriodo(int anoInicio, int anoFim, UUID idIgreja);
}