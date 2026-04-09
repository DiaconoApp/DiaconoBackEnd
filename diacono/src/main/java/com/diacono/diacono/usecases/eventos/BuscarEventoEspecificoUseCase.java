package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.evento.EventoCompletoDTO;
import com.diacono.diacono.applications.mappers.evento.EventoMapper;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.eventos.validation.ValidarIdExternoPreenchido;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarEventoEspecificoUseCase {

    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;
    private final ValidarIdExternoPreenchido validarIdExternoPreenchido;

    public BuscarEventoEspecificoUseCase(EventoRepository eventoRepository, EventoMapper eventoMapper, ValidarIdExternoPreenchido validarIdExternoPreenchido) {
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
        this.validarIdExternoPreenchido = validarIdExternoPreenchido;
    }

    public EventoCompletoDTO execute(UUID id) {

        validarIdExternoPreenchido.validarIdExternoPreenchido(id);

        Evento evento = eventoRepository.findByIdExterno(id)
                .orElseThrow(() -> new ObjectNotFoundException("Evento não encontrado"));

        return eventoMapper.paraEventoCompletoDTO(evento);
    }
}
