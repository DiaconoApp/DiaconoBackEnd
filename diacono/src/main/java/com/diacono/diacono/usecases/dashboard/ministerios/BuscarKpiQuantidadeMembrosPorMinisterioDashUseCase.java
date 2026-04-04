package com.diacono.diacono.usecases.dashboard.ministerios;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashQuantidadeMembrosDTO;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.MembroMinisterioService;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscarKpiQuantidadeMembrosPorMinisterioDashUseCase {

    private final MembroMinisterioService membroMinisterioService;
    private final DashboardPeriodoValidator periodoValidator;

    public BuscarKpiQuantidadeMembrosPorMinisterioDashUseCase(MembroMinisterioService membroMinisterioService, DashboardPeriodoValidator periodoValidator) {
        this.membroMinisterioService = membroMinisterioService;
        this.periodoValidator = periodoValidator;
    }

    public List<MinisterioDashQuantidadeMembrosDTO> execute(int anoInicio, int anoFim) {

        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        List<MinisterioDashQuantidadeMembrosDTO> response =
                membroMinisterioService.ministerioBuscarDashQuantidadeMembro(anoInicio, anoFim);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;
    }
}