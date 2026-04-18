package com.diacono.diacono.infrastructure.persistence.EscalaEvento;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoConsolidadoDTO;
import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoEscaladoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.infrastructure.persistence.dtos.EscalaEventoEscaladoQueryResult;
import com.diacono.diacono.infrastructure.persistence.dtos.EscalaEventoQueryResult;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class EscalaEventoRepositoryImpl implements EscalaEventoRepository {
    private final EscalaEventoJpaRepository jpaRepository;

    public EscalaEventoRepositoryImpl(EscalaEventoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<EscalaEventoConsolidadoDTO> findEscalaEventoConsolidadoByPeriodo(UUID idIgreja, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, EnumStatusEvento status, UUID ministerioId, String nomeEvento) {
        List<EscalaEventoQueryResult> queryResults = jpaRepository.findEscalaEventoConsolidadoByPeriodo(idIgreja, dataHoraInicio, dataHoraFim, status, ministerioId, nomeEvento);

        return queryResults.stream()
                .map(result ->  new EscalaEventoConsolidadoDTO(
                        result.idEventoExterno(),
                        result.nomeReuniao(),
                        result.dataHoraFim(),
                        result.dataHoraInicio(),
                        result.ministeriosEscalados(),
                        result.ministeriosEscaladosConfirmados(),
                        result.status()
                ))
                .toList();
    }

    public List<EscalaEventoEscaladoDTO> findEscalaEventoEscaladoByEventoId(UUID idIgreja, UUID eventoId) {
        List<EscalaEventoEscaladoQueryResult> queryResults = jpaRepository.findEscalaEventoEscaladoByEventoId(idIgreja, eventoId);

        return queryResults.stream()
                .map(result -> new EscalaEventoEscaladoDTO(
                        result.idExternoMinisterio(),
                        result.nomeMinisterio(),
                        result.idExternoEscalaEvento(),
                        result.isMinisterioEscalado()
                ))
                .toList();
    }

    public UUID findMinisterioIdByEscalaEventoId(UUID idIgreja, UUID escalaEventoId) {
        return jpaRepository.findMinisterioIdByEscalaEventoId(idIgreja, escalaEventoId);
    };
}
