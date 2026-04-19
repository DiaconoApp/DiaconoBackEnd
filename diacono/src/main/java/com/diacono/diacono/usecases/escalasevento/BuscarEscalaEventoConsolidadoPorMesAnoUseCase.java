package com.diacono.diacono.usecases.escalasevento;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoConsolidadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.usecases.escalasevento.validation.ValidarMesEAno;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class BuscarEscalaEventoConsolidadoPorMesAnoUseCase {

    private final EscalaEventoRepository escalaEventoRepository;
    private final ValidarMesEAno validarMesEAno;

    public BuscarEscalaEventoConsolidadoPorMesAnoUseCase(
            EscalaEventoRepository escalaEventoRepository,
            ValidarMesEAno validarMesEAno
    ) {
        this.escalaEventoRepository = escalaEventoRepository;
        this.validarMesEAno = validarMesEAno;
    }

    public List<EscalaEventoConsolidadoDTO> execute(UUID idIgreja, Integer mes, Integer ano, EnumStatusEvento status, UUID ministerioId, String nomeEvento) {
        validarMesEAno.validarMesEAno(mes, ano);

        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDateTime inicioMes = anoMes.atDay(1).atStartOfDay();
        LocalDateTime fimMes = anoMes.atEndOfMonth().atTime(23, 59, 59);

        return escalaEventoRepository
                .findEscalaEventoConsolidadoByPeriodo(idIgreja, inicioMes, fimMes, status, ministerioId, nomeEvento);
    }
}