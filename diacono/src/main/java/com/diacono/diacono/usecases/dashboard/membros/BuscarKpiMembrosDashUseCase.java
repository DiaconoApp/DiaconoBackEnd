package com.diacono.diacono.usecases.dashboard.membros;

import com.diacono.diacono.applications.dtos.ministerio.KpisMembrosDTO;
import com.diacono.diacono.applications.dtos.membro.MembroKpiResponseDTO;
import com.diacono.diacono.usecases.MembroService;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.stereotype.Service;

@Service
public class BuscarKpiMembrosDashUseCase {

    private final MembroService membroService;
    private final DashboardPeriodoValidator periodoValidator;

    public BuscarKpiMembrosDashUseCase(MembroService membroService, DashboardPeriodoValidator periodoValidator) {
        this.membroService = membroService;
        this.periodoValidator = periodoValidator;
    }

    public KpisMembrosDTO execute(int anoInicio, int anoFim) {
        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        MembroKpiResponseDTO kpis = membroService.buscarKpis(anoInicio, anoFim);

        long membrosAtivos = kpis.membrosAtivos();
        long membrosInativos = kpis.membrosInativos();
        long totalMembrosAnoInicio = kpis.totalAnoInicio();
        long totalMembros = membrosAtivos + membrosInativos;

        long membrosNovos = (anoInicio == anoFim) ? totalMembrosAnoInicio : totalMembros;

        double resultado = Math.round((membrosAtivos - membrosInativos) * 100.0 / totalMembros);
        double retencao = membrosInativos == 0 ? 100 : resultado;

        return new KpisMembrosDTO(membrosAtivos, membrosNovos, retencao);
    }
}