package com.diacono.diacono.usecases.dashboard.ministerios;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashQuantidadeMembrosDTO;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarKpiQuantidadeMembrosPorMinisterioDashUseCase {

    private final MembroMinisterioRepository membroMinisterioRepository;
    private final DashboardPeriodoValidator periodoValidator;
    private final JwtUtils jwtUtils;

    public BuscarKpiQuantidadeMembrosPorMinisterioDashUseCase(MembroMinisterioRepository membroMinisterioRepository, DashboardPeriodoValidator periodoValidator, JwtUtils jwtUtils) {
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.periodoValidator = periodoValidator;
        this.jwtUtils = jwtUtils;
    }

    @Cacheable(cacheNames = "dashboard:ministerios:quantidade-membros", keyGenerator = "dashboardCacheKey", sync = true)
    public List<MinisterioDashQuantidadeMembrosDTO> execute(int anoInicio, int anoFim) {

        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        List<MinisterioDashQuantidadeMembrosDTO> response = ministerioBuscarDashQuantidadeMembro(anoInicio, anoFim);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;
    }


    private List<MinisterioDashQuantidadeMembrosDTO> ministerioBuscarDashQuantidadeMembro(int anoInicio, int anoFim){

        UUID igrejaId = jwtUtils.getIgrejaId();

        return membroMinisterioRepository.buscarQuantidadeMembros(anoInicio, anoFim, igrejaId);
    }
}