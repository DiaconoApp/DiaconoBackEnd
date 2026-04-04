package com.diacono.diacono.usecases.dashboard.ministerios;

import com.diacono.diacono.applications.dtos.evento.EventoKpiDTO;
import com.diacono.diacono.applications.dtos.ministerio.KpisMinisteriosDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioKpisResponseDTO;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.EventoService;
import com.diacono.diacono.usecases.MinisterioService;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscarKpiMinisteriosDashUseCase {

    private final MinisterioService ministerioService;
    private final EventoService eventoService;
    private final DashboardPeriodoValidator periodoValidator;

    public BuscarKpiMinisteriosDashUseCase(MinisterioService ministerioService, EventoService eventoService, DashboardPeriodoValidator periodoValidator) {
        this.ministerioService = ministerioService;
        this.eventoService = eventoService;
        this.periodoValidator = periodoValidator;
    }

    public KpisMinisteriosDTO execute(int anoInicio, int anoFim) {

        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        MinisterioKpisResponseDTO kpiMinisterio = ministerioService.ministerioBuscarKpis(anoFim);
        List<EventoKpiDTO> kpiEvento = eventoService.buscarKpisEvento(anoInicio, anoFim);

        if (kpiMinisterio == null || kpiEvento == null || kpiEvento.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        EventoKpiDTO eventoRetido = kpiEvento.get(0);

        return new KpisMinisteriosDTO(eventoRetido, kpiMinisterio);
    }
}