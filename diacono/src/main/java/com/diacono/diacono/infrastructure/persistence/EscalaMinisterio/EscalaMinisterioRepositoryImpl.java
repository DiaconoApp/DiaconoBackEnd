package com.diacono.diacono.infrastructure.persistence.EscalaMinisterio;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioConsolidadoDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioDTO;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.infrastructure.persistence.dtos.EscalaMinisterioConsolidadoQueryResult;
import com.diacono.diacono.infrastructure.persistence.dtos.EscalaMinisterioQueryResult;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
                        result.idEventoExterno(),
                        result.nomeReuniao(),
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
                        result.idExternoEscalaMinisterio(),
                        result.nomeReuniao(),
                        result.nomeMinisterio(),
                        result.dataHoraFim(),
                        result.dataHoraInicio(),
                        result.status()
                ))
                .toList();
    }
}

