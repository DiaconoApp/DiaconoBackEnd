package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.mappers.EventoMapper;
import com.diacono.diacono.domain.entities.Evento;
import com.diacono.diacono.infrastructure.persistence.EventoRepository;
import com.diacono.diacono.presentation.dto.response.EventoCompletoDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.diacono.diacono.application.validators.EventoValidator.validarIdExternoPreenchido;

@Service
public class BuscarEventoEspecificoUseCase {

    private final EventoRepository repository;
    private final EventoMapper mapper;

    public BuscarEventoEspecificoUseCase(EventoRepository repository, EventoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public EventoCompletoDTO buscarEventoEspecifico(UUID id){

        validarIdExternoPreenchido(id);
        Evento evento = repository.findByIdExterno(id);
        EventoCompletoDTO eventoResponse = mapper.paraEventoCompletoDTO(evento);

        return eventoResponse;
    }

}
