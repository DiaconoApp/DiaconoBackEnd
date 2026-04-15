package com.diacono.diacono.usecases.escalasevento;

import com.diacono.diacono.applications.dtos.escalas.EscalaEventoDTO;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class BuscarEscalaEventoPorMesAnoUseCase {

    private final EscalaEventoRepository escalaEventoRepository;

    public BuscarEscalaEventoPorMesAnoUseCase(
            EscalaEventoRepository escalaEventoRepository
    ) {
        this.escalaEventoRepository = escalaEventoRepository;
    }

    public List<EscalaEventoDTO> execute (UUID idIgreja, Integer mes, Integer ano) {
        validarMesEAno(mes, ano);

        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDateTime inicioMes = anoMes.atDay(1).atStartOfDay();
        LocalDateTime fimMes = anoMes.atEndOfMonth().atTime(23, 59, 59);

        return escalaEventoRepository
                .findEscalaEventoByPeriodo(idIgreja, inicioMes, fimMes);
    }

    private void validarMesEAno(int mes, int ano) {
        if (mes < 1 || mes > 12) {
            throw new FieldInvalidException("O mês precisa estar entre 1 e 12");
        }

        if (ano <= 0) {
            throw new FieldInvalidException("O ano precisa ser maior que 0");
        }
    }
}