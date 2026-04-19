package com.diacono.diacono.usecases.escalasevento.validation;

import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import org.springframework.stereotype.Service;

@Service
public class ValidarMesEAno {

    public void validarMesEAno(int mes, int ano) {
        if (mes < 1 || mes > 12) {
            throw new FieldInvalidException("O mês precisa estar entre 1 e 12");
        }

        if (ano <= 0) {
            throw new FieldInvalidException("O ano precisa ser maior que 0");
        }
    }
}
