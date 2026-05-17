package com.diacono.diacono.usecases.escalasevento;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoConsolidadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.usecases.escalasevento.validation.ValidarMesEAno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BuscarEscalaEventoConsolidadoPorMesAnoUseCase {
    private static final Logger logger = LoggerFactory.getLogger(BuscarEscalaEventoConsolidadoPorMesAnoUseCase.class);

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
        validarIgrejaId(idIgreja);
        validarMesEAno.validarMesEAno(mes, ano);

        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDateTime inicioMes = anoMes.atDay(1).atStartOfDay();
        LocalDateTime fimMes = anoMes.atEndOfMonth().atTime(23, 59, 59);

        String nomeEventoNormalizado = normalizarNomeEvento(nomeEvento);
        logger.info("Consulta consolidado escalas evento: igrejaId=[{}], mes=[{}], ano=[{}], status=[{}], ministerioId=[{}], filtroNomeEvento=[{}]",
                idIgreja, mes, ano, status, ministerioId, nomeEventoNormalizado != null);

        return escalaEventoRepository
                .findEscalaEventoConsolidadoByPeriodo(idIgreja, inicioMes, fimMes, status, ministerioId, nomeEventoNormalizado);
    }

    private void validarIgrejaId(UUID idIgreja) {
        if (idIgreja == null) {
            logger.warn("Consulta consolidado escalas evento sem igrejaId");
            throw new FieldInvalidException("O Id da igreja é obrigatório");
        }
    }

    private String normalizarNomeEvento(String nomeEvento) {
        return Optional.ofNullable(nomeEvento)
                .map(String::trim)
                .filter(nome -> !nome.isBlank())
                .orElse(null);
    }
}
