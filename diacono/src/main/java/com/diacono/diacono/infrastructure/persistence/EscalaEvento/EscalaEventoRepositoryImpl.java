package com.diacono.diacono.infrastructure.persistence.EscalaEvento;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoDTO;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.infrastructure.persistence.dto.EscalaEventoQueryResult;
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
    public List<EscalaEventoDTO> findEscalaEventoByPeriodo(UUID idIgreja, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        List<EscalaEventoQueryResult> queryResults = jpaRepository.findEscalaEventoByPeriodo(idIgreja, dataHoraInicio, dataHoraFim);

        return queryResults.stream()
                .map(result ->  new EscalaEventoDTO(
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
}
