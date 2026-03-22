package com.diacono.diacono.application.validators;

import com.diacono.diacono.domain.entities.Membro;
import com.diacono.diacono.application.exceptions.ObjectSaveErrorException;

public class MembroValidator {

    public static void validaCriacao(Membro membro) {
        if (membro == null) {
            throw new ObjectSaveErrorException("Não foi possível cadastrar o usuário");
        }
    }
}
