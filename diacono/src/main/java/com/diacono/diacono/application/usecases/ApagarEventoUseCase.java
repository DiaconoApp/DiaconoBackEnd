package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.persistence.EventoRepository;
import com.diacono.diacono.presentation.dto.response.RestResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.diacono.diacono.application.validators.EventoValidator.validarIdExternoPreenchido;

@Service
public class ApagarEventoUseCase {

    private final EventoRepository repository;

    public ApagarEventoUseCase(EventoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RestResponseMessage apagarEvento(UUID idExterno){
        validarIdExternoPreenchido(idExterno);

        long deleteCount = repository.deleteByIdExterno(idExterno);

        if(deleteCount == 0){
            throw new ObjectNotFoundException("Não foi possível apagar o evento, verifique se o evento existe");
        }

        RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Evento apagado com sucesso");

        return message;

    }

}
