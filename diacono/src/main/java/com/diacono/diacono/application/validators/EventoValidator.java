package com.diacono.diacono.application.validators;

import com.diacono.diacono.application.exceptions.FieldInvalidException;

import java.util.UUID;

public class EventoValidator {

    public static void validarIdExternoPreenchido(UUID idExterno){
        if(idExterno == null){
            throw new FieldInvalidException("O id do evento precisa ser informado");
        }
    }
}
