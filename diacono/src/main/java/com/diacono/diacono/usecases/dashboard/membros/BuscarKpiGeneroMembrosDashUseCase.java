package com.diacono.diacono.usecases.dashboard.membros;

import com.diacono.diacono.applications.dtos.dashboard.DashboardGeneroMembroDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDashGeneroDTO;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.MembroService;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.stereotype.Service;

@Service
public class BuscarKpiGeneroMembrosDashUseCase {

    private final MembroService membroService;
    private final DashboardPeriodoValidator periodoValidator;

    public BuscarKpiGeneroMembrosDashUseCase(
            MembroService membroService,
            DashboardPeriodoValidator periodoValidator
    ) {
        this.membroService = membroService;
        this.periodoValidator = periodoValidator;
    }

    public DashboardGeneroMembroDTO execute(int anoInicio, int anoFim) {

        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        MembroDashGeneroDTO genero = membroService.buscarDashGenero(anoFim);

        if (genero == null) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        long total = genero.masculino() + genero.feminino();

        if (total == 0) {
            return new DashboardGeneroMembroDTO(0, 0);
        }

        double masculinoPercent = (double) genero.masculino() / total * 100;
        double femininoPercent = (double) genero.feminino() / total * 100;

        return new DashboardGeneroMembroDTO(masculinoPercent, femininoPercent);
    }
}