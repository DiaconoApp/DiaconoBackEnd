package com.diacono.diacono.usecases.dashboard.ministerios;

import com.diacono.diacono.applications.dtos.evento.EventoKpiDTO;
import com.diacono.diacono.applications.dtos.ministerio.KpisMinisteriosDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioKpisResponseDTO;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarKpiMinisteriosDashUseCase {

    private final MinisteriosRepository ministeriosRepository;
    private final DashboardPeriodoValidator periodoValidator;
    private final JwtUtils jwtUtils;
    private final EventoRepository eventoRepository;

    public BuscarKpiMinisteriosDashUseCase(MinisteriosRepository ministeriosRepository, DashboardPeriodoValidator periodoValidator, JwtUtils jwtUtils, EventoRepository eventoRepository) {
        this.ministeriosRepository = ministeriosRepository;
        this.periodoValidator = periodoValidator;
        this.jwtUtils = jwtUtils;
        this.eventoRepository = eventoRepository;
    }

    @Cacheable(cacheNames = "dashboard:ministerios:kpis", keyGenerator = "dashboardCacheKey", sync = true)
    public KpisMinisteriosDTO execute(int anoInicio, int anoFim) {

        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        MinisterioKpisResponseDTO kpiMinisterio = ministerioBuscarKpis(anoFim);
        List<EventoKpiDTO> kpiEvento = buscarKpisEvento(anoInicio, anoFim);

        if (kpiMinisterio == null || kpiEvento == null || kpiEvento.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        EventoKpiDTO eventoRetido = kpiEvento.get(0);

        return new KpisMinisteriosDTO(eventoRetido, kpiMinisterio);
    }

    private MinisterioKpisResponseDTO ministerioBuscarKpis(int anoFim) {
        UUID igrejaId = jwtUtils.getIgrejaId();

        return ministeriosRepository.buscarKpis(igrejaId, anoFim);
    }


    private List<EventoKpiDTO> buscarKpisEvento(int anoInicio, int anoFim) {

        UUID idIgreja = jwtUtils.getIgrejaId();

        return eventoRepository.buscarKpisEvento(anoInicio, anoFim, idIgreja);
    }
}