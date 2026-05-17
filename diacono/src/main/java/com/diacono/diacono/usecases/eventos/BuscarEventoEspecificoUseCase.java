package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.evento.EventoCompletoDTO;
import com.diacono.diacono.applications.mappers.evento.EventoMapper;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.eventos.validation.ValidarIdExternoPreenchido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarEventoEspecificoUseCase {

    private static final Logger logger = LoggerFactory.getLogger(BuscarEventoEspecificoUseCase.class);

    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;
    private final ValidarIdExternoPreenchido validarIdExternoPreenchido;

    public BuscarEventoEspecificoUseCase(EventoRepository eventoRepository, EventoMapper eventoMapper, ValidarIdExternoPreenchido validarIdExternoPreenchido) {
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
        this.validarIdExternoPreenchido = validarIdExternoPreenchido;
    }

    public EventoCompletoDTO execute(UUID id, UUID igrejaId) {

        validarIdExternoPreenchido.validarIdExternoPreenchido(id);

        Evento evento = eventoRepository.findByIdExterno(id)
                .orElseThrow(() -> new ObjectNotFoundException("Evento não encontrado"));

        if (igrejaId != null && (evento.getIgreja() == null || evento.getIgreja().getIdExterno() == null || !igrejaId.equals(evento.getIgreja().getIdExterno()))) {
            logger.warn("Tentativa de consulta de evento fora do escopo da igreja autenticada. eventoId=[{}] igrejaId=[{}]", id, igrejaId);
            throw new ObjectNotFoundException("Evento não encontrado");
        }

        return eventoMapper.paraEventoCompletoDTO(evento);
    }
}
