package com.diacono.diacono.usecases.dashboard.membros;

import com.diacono.diacono.applications.dtos.ministerio.KpisMembrosDTO;
import com.diacono.diacono.applications.dtos.membro.MembroKpiResponseDTO;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarKpiMembrosDashUseCase {

    private final DashboardPeriodoValidator periodoValidator;
    private final MembroRepository membroRepository;
    private final JwtUtils jwtUtils;

    public BuscarKpiMembrosDashUseCase(DashboardPeriodoValidator periodoValidator, MembroRepository membroRepository, JwtUtils jwtUtils) {
        this.periodoValidator = periodoValidator;
        this.membroRepository = membroRepository;
        this.jwtUtils = jwtUtils;
    }

    @Cacheable(cacheNames = "dashboard:membros:kpis", keyGenerator = "dashboardCacheKey", sync = true)
    public KpisMembrosDTO execute(int anoInicio, int anoFim) {
        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        MembroKpiResponseDTO kpis = buscarKpis(anoInicio, anoFim);

        long membrosAtivos = kpis.membrosAtivos();
        long membrosInativos = kpis.membrosInativos();
        long totalMembrosAnoInicio = kpis.totalAnoInicio();
        long totalMembros = membrosAtivos + membrosInativos;

        long membrosNovos = (anoInicio == anoFim) ? totalMembrosAnoInicio : totalMembros;

        double resultado = Math.round((membrosAtivos - membrosInativos) * 100.0 / totalMembros);
        double retencao = membrosInativos == 0 ? 100 : resultado;

        return new KpisMembrosDTO(membrosAtivos, membrosNovos, retencao);
    }

    public MembroKpiResponseDTO buscarKpis(int anoInicio, int anoFim){

        UUID idExternoIgreja = jwtUtils.getIgrejaId();

        return membroRepository.buscarKpisMembros(idExternoIgreja, anoInicio, anoFim);
    }

}