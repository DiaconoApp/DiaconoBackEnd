package com.diacono.diacono.usecases.dashboard.ministerios;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioEventoDashDTO;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class buscarKpiQuantidadeEventosPorMinisterioDashUseCase {

    private final DashboardPeriodoValidator periodoValidator;
    private final JwtUtils jwtUtils;
    private final EventoRepository eventoRepository;

    public buscarKpiQuantidadeEventosPorMinisterioDashUseCase(DashboardPeriodoValidator periodoValidator, JwtUtils jwtUtils, EventoRepository eventoRepository) {
        this.periodoValidator = periodoValidator;
        this.jwtUtils = jwtUtils;
        this.eventoRepository = eventoRepository;
    }

    @Cacheable(cacheNames = "dashboard:ministerios:quantidade-eventos", keyGenerator = "dashboardCacheKey", sync = true)
    public List<MinisterioEventoDashDTO> execute(int anoInicio, int anoFim) {

        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        List<MinisterioEventoDashDTO> response = ministerioBuscarDashQuantidadeEventos(anoInicio, anoFim);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;
    }

    public List<MinisterioEventoDashDTO> ministerioBuscarDashQuantidadeEventos(int anoInicio, int anoFim){

        UUID idIgreja = jwtUtils.getIgrejaId();

        List<MinisterioEventoDashDTO> response = eventoRepository.contarEventosPorMinisterioNoPeriodo(anoInicio, anoFim, idIgreja);


        return response;

    }
}
