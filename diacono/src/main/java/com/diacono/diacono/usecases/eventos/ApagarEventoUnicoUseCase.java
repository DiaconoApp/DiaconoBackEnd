package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.eventos.validation.ValidarIdExternoPreenchido;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ApagarEventoUnicoUseCase {

    private final EventoRepository eventoRepository;
    private final ValidarIdExternoPreenchido validarIdExternoPreenchido;

    public ApagarEventoUnicoUseCase(
            EventoRepository eventoRepository,
            ValidarIdExternoPreenchido validarIdExternoPreenchido
    ) {
        this.eventoRepository = eventoRepository;
        this.validarIdExternoPreenchido = validarIdExternoPreenchido;
    }

    @Transactional
    public RestResponseMessageDTO execute(UUID id) {

        validarIdExternoPreenchido.validarIdExternoPreenchido(id);

        long deleteCount = eventoRepository.deleteByIdExterno(id);

        if (deleteCount == 0) {
            throw new ObjectNotFoundException("Não foi possível apagar o evento, verifique se o evento existe");
        }

        return new RestResponseMessageDTO(HttpStatus.OK, "Evento apagado com sucesso");
    }
}