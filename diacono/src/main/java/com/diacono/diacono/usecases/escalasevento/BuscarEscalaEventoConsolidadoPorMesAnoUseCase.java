package com.diacono.diacono.usecases.escalasevento;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoConsolidadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class BuscarEscalaEventoConsolidadoPorMesAnoUseCase {

    private final EscalaEventoRepository escalaEventoRepository;

    public BuscarEscalaEventoConsolidadoPorMesAnoUseCase(
            EscalaEventoRepository escalaEventoRepository
    ) {
        this.escalaEventoRepository = escalaEventoRepository;
    }

    public List<EscalaEventoConsolidadoDTO> execute(UUID idIgreja, Integer mes, Integer ano, EnumStatusEvento status, UUID ministerioId, String nomeEvento) {
        validarMesEAno(mes, ano);

        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDateTime inicioMes = anoMes.atDay(1).atStartOfDay();
        LocalDateTime fimMes = anoMes.atEndOfMonth().atTime(23, 59, 59);

        return escalaEventoRepository
                .findEscalaEventoConsolidadoByPeriodo(idIgreja, inicioMes, fimMes, status, ministerioId, nomeEvento);
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