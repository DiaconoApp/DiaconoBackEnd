package com.diacono.diacono.infrastructure.persistence.Evento;

import com.diacono.diacono.applications.dtos.evento.EventoKpiDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioEventoDashDTO;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.entity.Recorrencia;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.domain.repository.EventoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EventoRepositoryImpl implements EventoRepository {

    private final EventoJpaRepository jpaRepository;

    public EventoRepositoryImpl(EventoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Evento> findByPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim, UUID igrejaFk) {
        return jpaRepository.findByPeriodo(dataInicio, dataFim, igrejaFk);
    }

    @Override
    public Optional<Evento> findByIdExterno(UUID idExterno) {
        return Optional.ofNullable(jpaRepository.findByIdExterno(idExterno));
    }

    @Override
    public long deleteByIdExterno(UUID idExterno) {
        return jpaRepository.deleteByIdExterno(idExterno);
    }

    @Override
    public List<Evento> findByPeriodoAndRecorrencia(Recorrencia recorrencia, LocalDateTime dataInicio, UUID igrejaFk) {
        return jpaRepository.findByPeriodoAndRecorrencia(recorrencia, dataInicio, igrejaFk);
    }

    @Override
    public Evento save(Evento evento) {
        return jpaRepository.save(evento);
    }

    @Override
    public List<Evento> saveAll(List<Evento> eventos) {
        return jpaRepository.saveAll(eventos);
    }

    @Override
    public void deleteAll(List<Evento> eventos) {
        jpaRepository.deleteAll(eventos);
    }

    @Override
    public void updateStatusByEventoId(UUID eventoId, EnumStatusEvento status) {
        jpaRepository.updateStatusByEventoId(eventoId, status);
    }

    @Override
    public List<EventoKpiDTO> buscarKpisEvento(int anoInicio, int anoFim, UUID idIgreja) {
        return jpaRepository.buscarKpisEvento(anoInicio, anoFim, idIgreja);
    }

    @Override
    public List<MinisterioEventoDashDTO> contarEventosPorMinisterioNoPeriodo(int anoInicio, int anoFim, UUID idIgreja) {
        return jpaRepository.contarEventosPorMinisterioNoPeriodo(anoInicio, anoFim, idIgreja);
    }
}