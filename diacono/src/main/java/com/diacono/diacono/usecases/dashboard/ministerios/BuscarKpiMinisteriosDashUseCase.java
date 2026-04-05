package com.diacono.diacono.usecases.dashboard.ministerios;

import com.diacono.diacono.applications.dtos.evento.EventoKpiDTO;
import com.diacono.diacono.applications.dtos.ministerio.KpisMinisteriosDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioKpisResponseDTO;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.MinisterioService;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarKpiMinisteriosDashUseCase {

    private final MinisterioService ministerioService;
    private final DashboardPeriodoValidator periodoValidator;
    private final JwtUtils jwtUtils;
    private final EventoRepository eventoRepository;

    public BuscarKpiMinisteriosDashUseCase(MinisterioService ministerioService, DashboardPeriodoValidator periodoValidator, JwtUtils jwtUtils, EventoRepository eventoRepository) {
        this.ministerioService = ministerioService;
        this.periodoValidator = periodoValidator;
        this.jwtUtils = jwtUtils;
        this.eventoRepository = eventoRepository;
    }

    public KpisMinisteriosDTO execute(int anoInicio, int anoFim) {

        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        MinisterioKpisResponseDTO kpiMinisterio = ministerioService.ministerioBuscarKpis(anoFim);
        List<EventoKpiDTO> kpiEvento = buscarKpisEvento(anoInicio, anoFim);

        if (kpiMinisterio == null || kpiEvento == null || kpiEvento.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        EventoKpiDTO eventoRetido = kpiEvento.get(0);

        return new KpisMinisteriosDTO(eventoRetido, kpiMinisterio);
    }

    private List<EventoKpiDTO> buscarKpisEvento(int anoInicio, int anoFim) {

        UUID idIgreja = jwtUtils.getIgrejaId();

        List<EventoKpiDTO> kpisEvento = eventoRepository.buscarKpisEvento(anoInicio, anoFim, idIgreja);

        return kpisEvento;
    }
}