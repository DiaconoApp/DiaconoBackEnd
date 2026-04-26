package com.diacono.diacono.usecases.dashboard.ministerios;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashEvolucaoDTO;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarKpiEvolucaoMinisteriosDashUseCase {

    private final MembroMinisterioRepository membroMinisterioRepository;
    private final DashboardPeriodoValidator periodoValidator;
    private final JwtUtils jwtUtils;

    public BuscarKpiEvolucaoMinisteriosDashUseCase(MembroMinisterioRepository membroMinisterioRepository, DashboardPeriodoValidator periodoValidator, JwtUtils jwtUtils) {
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.periodoValidator = periodoValidator;
        this.jwtUtils = jwtUtils;
    }

    @Cacheable(cacheNames = "dashboard:ministerios:evolucao", keyGenerator = "dashboardCacheKey", sync = true)
    public List<MinisterioDashEvolucaoDTO> execute(int anoInicio, int anoFim, UUID idMinisterio) {

        if (idMinisterio == null) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        List<MinisterioDashEvolucaoDTO> response = ministerioBuscarDashEvolucao(anoInicio, anoFim, idMinisterio);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;
    }

    private List<MinisterioDashEvolucaoDTO> ministerioBuscarDashEvolucao(int anoInicio, int anoFim, UUID idMinisterio){

        UUID idIgreja = jwtUtils.getIgrejaId();

        if(anoInicio == anoFim){
            List<MinisterioDashEvolucaoDTO> response = membroMinisterioRepository.buscarDashEvolucaoUmAno(anoFim, idMinisterio, idIgreja);
            return response;
        }

        List<MinisterioDashEvolucaoDTO> response = membroMinisterioRepository.buscarDashEvolucaoPeriodo(anoInicio,anoFim, idMinisterio, idIgreja);

        return response;

    }
}