package com.diacono.diacono.application.validators;

import com.diacono.diacono.application.exceptions.FieldInvalidException;

public class DateValidator {

    public static void validarAnoInicioEFim(int anoInicio, int anoFim) {
        if (anoInicio > anoFim) {
            throw new FieldInvalidException("Ano de início não pode ser maior que ano de fim.");
        }

        if (anoInicio <= 0 || anoFim <= 0) {
            throw new FieldInvalidException("Ano de início e ano de fim devem ser informados.");
        }
    }

}
