package com.diacono.diacono.usecases.dashboard.membros;

import com.diacono.diacono.applications.dtos.dashboard.DashboardFaixaEtariaMembroDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDashFaixaEtariaDTO;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.MembroService;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.stereotype.Service;

@Service
public class BuscarKpiFaixaEtariaMembrosDashUseCase {

    private final MembroService membroService;
    private final DashboardPeriodoValidator periodoValidator;

    public BuscarKpiFaixaEtariaMembrosDashUseCase(MembroService membroService, DashboardPeriodoValidator periodoValidator) {
        this.membroService = membroService;
        this.periodoValidator = periodoValidator;
    }

    public DashboardFaixaEtariaMembroDTO execute(int anoInicio, int anoFim) {
        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        MembroDashFaixaEtariaDTO faixaEtaria = membroService.buscarDashFaixaEtaria(anoFim);

        if (faixaEtaria == null) {
            throw new ObjectNotFoundException("Nenhum dado encontrado.");
        }

        long total = faixaEtaria.criancas()
                + faixaEtaria.adolescentes()
                + faixaEtaria.jovens()
                + faixaEtaria.adultos()
                + faixaEtaria.idosos();

        if (total == 0) {
            return new DashboardFaixaEtariaMembroDTO(0, 0, 0, 0, 0);
        }

        return new DashboardFaixaEtariaMembroDTO(
                (faixaEtaria.criancas() * 100) / total,
                (faixaEtaria.adolescentes() * 100) / total,
                (faixaEtaria.jovens() * 100) / total,
                (faixaEtaria.adultos() * 100) / total,
                (faixaEtaria.idosos() * 100) / total
        );
    }
}