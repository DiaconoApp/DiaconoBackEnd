package com.diacono.diacono.usecases.eventos.validation;

import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ValidarIdExternoPreenchido {

    public void validarIdExternoPreenchido(UUID idExterno){
        if(idExterno == null){
            throw new FieldInvalidException("O id do evento precisa ser informado");
        }
    }
}
