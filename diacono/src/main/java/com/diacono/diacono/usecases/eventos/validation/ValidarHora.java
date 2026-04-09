package com.diacono.diacono.usecases.eventos.validation;

import com.diacono.diacono.infrastructure.exceptions.TimeInvalidException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ValidarHora {

    public void validaHoraInicioMenorHoraFim(LocalDateTime inicio, LocalDateTime fim) {
        if (fim.isBefore(inicio)) {
            throw new TimeInvalidException("O horário de término do evento precisa ser maior que o horário de início");
        }
    }

    public void validarHoraFuturo(LocalDateTime inicio, LocalDateTime fim) {

        LocalDateTime hojeComMargem = LocalDateTime.now().plusMinutes(1);

        if (inicio.isBefore(hojeComMargem) && fim.isBefore(hojeComMargem)) {
            throw new TimeInvalidException("Não é possível cadastrar eventos com horários passados.");
        }
    }
}
