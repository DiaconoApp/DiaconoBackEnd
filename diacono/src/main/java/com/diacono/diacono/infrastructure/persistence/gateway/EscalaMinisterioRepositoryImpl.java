package com.diacono.diacono.infrastructure.persistence.gateway;

import com.diacono.diacono.applications.dtos.escalaministerio.*;
import com.diacono.diacono.domain.entity.EscalaMinisterio;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.infrastructure.persistence.springdata.EscalaMinisterioJpaRepository;
import com.diacono.diacono.infrastructure.persistence.projection.EscalaMembroMinisterioQueryResult;
import com.diacono.diacono.infrastructure.persistence.projection.EscalaMinisterioConsolidadoQueryResult;
import com.diacono.diacono.infrastructure.persistence.projection.EscalaMinisterioQueryResult;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Repository
public class EscalaMinisterioRepositoryImpl implements EscalaMinisterioRepository {

    private final EscalaMinisterioJpaRepository jpaRepository;

    public EscalaMinisterioRepositoryImpl(EscalaMinisterioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<EscalaMinisterioConsolidadoDTO> findEscalaMinisterioConsolidadoByPeriodo(
            UUID igrejaId,
            LocalDateTime inicioMes,
            LocalDateTime fimMes,
            EnumStatusEscalaMinisterio status,
            List<UUID> listaMinisterios,
            String nomeEvento
    ) {
        List<EscalaMinisterioConsolidadoQueryResult> queryResults = jpaRepository.findEscalaMinisterioConsolidadoByPeriodo(
                igrejaId,
                inicioMes,
                fimMes,
                status,
                listaMinisterios,
                nomeEvento
        );

        return queryResults.stream()
                .map(result -> new EscalaMinisterioConsolidadoDTO(
                        result.idEvento(),
                        result.idExternoEscalaEvento(),
                        result.nomeReuniao(),
                        result.nomeMinisterio(),
                        result.dataHoraFim(),
                        result.dataHoraInicio(),
                        result.membrosEscalados(),
                        result.membrosEscaladosConfirmados(),
                        result.status()
                ))
                .toList();
    }

    @Override
    public List<EscalaMinisterioDTO> findEscalaMinisterioByPeriodo(UUID igrejaId, LocalDateTime inicioMes, LocalDateTime fimMes, EnumStatusEscalaMinisterio status, UUID membroId, UUID ministerioId, String nomeEvento) {
        List<EscalaMinisterioQueryResult> queryResults = jpaRepository.findEscalaMinisterioByPeriodo(
                igrejaId,
                inicioMes,
                fimMes,
                status,
                membroId,
                ministerioId,
                nomeEvento
        );

        return queryResults.stream()
                .map(result -> new EscalaMinisterioDTO(
                        result.idExternoEvento(),
                        result.idExternoEscalaMinisterio(),
                        result.nomeReuniao(),
                        result.nomeMinisterio(),
                        result.dataHoraFim(),
                        result.dataHoraInicio(),
                        result.status()
                ))
                .toList();
    }

    @Override
    public List<EscalaMembroMinisterioDTO> findEscalaMembroMinisterioByEscalaEventoId(UUID igrejaId, UUID escalaEventoId) {
        List<EscalaMembroMinisterioQueryResult> queryResults = jpaRepository.findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId);

        return queryResults.stream()
                .map(result -> new EscalaMembroMinisterioDTO(
                        result.membroMinisterioId(),
                        result.nomeMembro(),
                        result.status(),
                        result.isMembroOcupado()
                ))
                .toList();
    }

    @Override
    public List<UUID> findMembrosMinisterioOcupadosByEscalaEventoId(UUID igrejaId, UUID escalaEventoId) {
        return jpaRepository.findMembrosMinisterioOcupadosByEscalaEventoId(igrejaId, escalaEventoId);
    }

    @Override
    public Map<UUID, MembroMinisterio> findMembrosMinisterioByEscalaEventoIdAndIds (UUID igrejaId, UUID escalaEventoId, List<UUID> idsMembrosMinisterio) {
        return jpaRepository
                .findMembrosMinisterioByEscalaEventoIdAndIds(igrejaId, escalaEventoId, idsMembrosMinisterio)
                .stream()
                .collect(java.util.stream.Collectors.toMap(MembroMinisterio::getIdExterno, Function.identity()));
    }

    @Override
    public void replaceEscalaMinisterioByEscalaEventoId(UUID igrejaId, UUID escalaEventoId, List<EscalaMinisterio> escalasParaSalvar) {
        jpaRepository.deleteByEscalaEventoIdAndIgrejaId(igrejaId, escalaEventoId);

        jpaRepository.saveAll(escalasParaSalvar);
    }

    @Override
    public boolean areAllConfirmadosByEscalaEventoId(UUID escalaEventoId) {
        return jpaRepository.areAllConfirmadosByEscalaEventoId(escalaEventoId);
    }
}

