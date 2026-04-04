package com.diacono.diacono.usecases.dashboard.membros;

import com.diacono.diacono.applications.dtos.membro.MembroDashEvolucaoDTO;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.MembroService;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscarKpiEvolucaoMembrosDashUseCase {

    private final MembroService membroService;
    private final DashboardPeriodoValidator periodoValidator;

    public BuscarKpiEvolucaoMembrosDashUseCase(
            MembroService membroService,
            DashboardPeriodoValidator periodoValidator
    ) {
        this.membroService = membroService;
        this.periodoValidator = periodoValidator;
    }

    public List<MembroDashEvolucaoDTO> execute(int anoInicio, int anoFim) {
        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        List<MembroDashEvolucaoDTO> response = membroService.buscarDashEvolucao(anoInicio, anoFim);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;
    }
}