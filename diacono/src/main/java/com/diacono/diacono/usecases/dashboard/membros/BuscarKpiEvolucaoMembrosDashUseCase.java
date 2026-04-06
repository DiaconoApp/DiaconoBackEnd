package com.diacono.diacono.usecases.dashboard.membros;

import com.diacono.diacono.applications.dtos.membro.MembroDashEvolucaoDTO;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarKpiEvolucaoMembrosDashUseCase {

    private final DashboardPeriodoValidator periodoValidator;
    private final MembroRepository membroRepository;
    private final JwtUtils jwtUtils;

    public BuscarKpiEvolucaoMembrosDashUseCase(DashboardPeriodoValidator periodoValidator, MembroRepository membroRepository, JwtUtils jwtUtils) {
        this.periodoValidator = periodoValidator;
        this.membroRepository = membroRepository;
        this.jwtUtils = jwtUtils;
    }

    public List<MembroDashEvolucaoDTO> execute(int anoInicio, int anoFim) {
        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        List<MembroDashEvolucaoDTO> response = buscarDashEvolucao(anoInicio, anoFim);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;
    }

    public List<MembroDashEvolucaoDTO> buscarDashEvolucao(int anoInicio, int anoFim) {

        UUID idExternoIgreja = jwtUtils.getIgrejaId();

        return membroRepository.buscarMembrosPorAno(idExternoIgreja, anoInicio, anoFim);
    }

}