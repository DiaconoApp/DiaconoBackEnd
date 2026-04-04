package com.diacono.diacono.usecases.dashboard.ministerios;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashEvolucaoDTO;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.MembroMinisterioService;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarKpiEvolucaoMinisteriosDashUseCase {

    private final MembroMinisterioService membroMinisterioService;
    private final DashboardPeriodoValidator periodoValidator;

    public BuscarKpiEvolucaoMinisteriosDashUseCase(MembroMinisterioService membroMinisterioService, DashboardPeriodoValidator periodoValidator) {
        this.membroMinisterioService = membroMinisterioService;
        this.periodoValidator = periodoValidator;
    }

    public List<MinisterioDashEvolucaoDTO> execute(int anoInicio, int anoFim, UUID idMinisterio) {

        if (idMinisterio == null) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        List<MinisterioDashEvolucaoDTO> response =
                membroMinisterioService.ministerioBuscarDashEvolucao(anoInicio, anoFim, idMinisterio);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;
    }
}