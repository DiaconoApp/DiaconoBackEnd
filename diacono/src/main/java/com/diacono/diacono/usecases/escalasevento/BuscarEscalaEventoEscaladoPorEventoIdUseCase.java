package com.diacono.diacono.usecases.escalasevento;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoEscaladoDTO;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarEscalaEventoEscaladoPorEventoIdUseCase {
    private static final Logger logger = LoggerFactory.getLogger(BuscarEscalaEventoEscaladoPorEventoIdUseCase.class);
    private final EscalaEventoRepository escalaEventoRepository;

    public BuscarEscalaEventoEscaladoPorEventoIdUseCase(
            EscalaEventoRepository escalaEventoRepository
    ) {
        this.escalaEventoRepository = escalaEventoRepository;
    }

    public List<EscalaEventoEscaladoDTO> execute(UUID idIgreja, UUID eventoId) {
        validarIds(idIgreja, eventoId);
        logger.info("Consulta escala evento escalado: igrejaId=[{}], eventoId=[{}]", idIgreja, eventoId);

        return escalaEventoRepository
                .findEscalaEventoEscaladoByEventoId(idIgreja, eventoId);
    }

    private void validarIds(UUID idIgreja, UUID eventoId) {
        if (idIgreja == null || eventoId == null) {
            logger.warn("Consulta escala evento escalado com ids inválidos: igrejaIdPresente=[{}], eventoIdPresente=[{}]",
                    idIgreja != null, eventoId != null);
            throw new FieldInvalidException("Ids de igreja e evento sao obrigatórios");
        }
    }
}
