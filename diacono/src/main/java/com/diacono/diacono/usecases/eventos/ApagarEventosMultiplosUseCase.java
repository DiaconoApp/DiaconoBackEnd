package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.eventos.validation.ValidarIdExternoPreenchido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ApagarEventosMultiplosUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ApagarEventosMultiplosUseCase.class);

    private final EventoRepository eventoRepository;
    private final ValidarIdExternoPreenchido validarIdExternoPreenchido;

    public ApagarEventosMultiplosUseCase(
            EventoRepository eventoRepository,
            ValidarIdExternoPreenchido validarIdExternoPreenchido
    ) {
        this.eventoRepository = eventoRepository;
        this.validarIdExternoPreenchido = validarIdExternoPreenchido;
    }

    @Transactional
    public RestResponseMessageDTO execute(UUID idEvento, UUID igrejaId) {

        validarIdExternoPreenchido.validarIdExternoPreenchido(idEvento);


        Evento evento = eventoRepository.findByIdExterno(idEvento)
                .orElseThrow(() -> new ObjectNotFoundException("Não foi possível apagar o evento, verifique se o evento existe"));

        if (evento.getIgreja() == null || evento.getIgreja().getIdExterno() == null || !igrejaId.equals(evento.getIgreja().getIdExterno())) {
            logger.warn("Tentativa de exclusão de evento fora do escopo da igreja autenticada. eventoId=[{}] igrejaId=[{}]", idEvento, igrejaId);
            throw new ObjectNotFoundException("Não foi possível apagar o evento, verifique se o evento existe");
        }

        List<Evento> eventos = eventoRepository.findByPeriodoAndRecorrencia(
                evento.getRecorrencia(),
                evento.getDataHoraInicio(),
                igrejaId
        );

        if (!eventos.contains(evento)) {
            eventos.add(evento);
        }

        eventoRepository.deleteAll(eventos);

        logger.info("Eventos apagados com sucesso. eventoId=[{}] igrejaId=[{}] quantidade=[{}]", idEvento, igrejaId, eventos.size());

        return new RestResponseMessageDTO(HttpStatus.OK, "Eventos apagados com sucesso");
    }
}