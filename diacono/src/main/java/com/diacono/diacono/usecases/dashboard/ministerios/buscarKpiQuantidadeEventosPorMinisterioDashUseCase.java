package com.diacono.diacono.usecases.dashboard.ministerios;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioEventoDashDTO;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.EventoService;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class buscarKpiQuantidadeEventosPorMinisterioDashUseCase {

    private final EventoService eventoService;
    private final DashboardPeriodoValidator periodoValidator;

    public buscarKpiQuantidadeEventosPorMinisterioDashUseCase(EventoService eventoService, DashboardPeriodoValidator periodoValidator) {
        this.eventoService = eventoService;
        this.periodoValidator = periodoValidator;
    }

    public List<MinisterioEventoDashDTO> execute(int anoInicio, int anoFim) {

        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        List<MinisterioEventoDashDTO> response = eventoService.ministerioBuscarDashQuantidadeEventos(anoInicio, anoFim);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;
    }
}
