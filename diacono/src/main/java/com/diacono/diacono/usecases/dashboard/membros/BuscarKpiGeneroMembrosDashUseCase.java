package com.diacono.diacono.usecases.dashboard.membros;

import com.diacono.diacono.applications.dtos.dashboard.DashboardGeneroMembroDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDashGeneroDTO;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarKpiGeneroMembrosDashUseCase {
    private static final Logger logger = LoggerFactory.getLogger(BuscarKpiGeneroMembrosDashUseCase.class);

    private final DashboardPeriodoValidator periodoValidator;
    private final MembroRepository membroRepository;
    private final JwtUtils jwtUtils;

    public BuscarKpiGeneroMembrosDashUseCase(DashboardPeriodoValidator periodoValidator, MembroRepository membroRepository, JwtUtils jwtUtils) {
        this.periodoValidator = periodoValidator;
        this.membroRepository = membroRepository;
        this.jwtUtils = jwtUtils;
    }

    public DashboardGeneroMembroDTO execute(int anoInicio, int anoFim) {

        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        MembroDashGeneroDTO genero = buscarDashGenero(anoFim);

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

    public MembroDashGeneroDTO buscarDashGenero(int anoFim) {

        UUID idExternoIgreja = jwtUtils.getIgrejaId();

        MembroDashGeneroDTO response = membroRepository.buscarMembrosPorGenero(idExternoIgreja, anoFim);

        logger.info("Consulta dashboard genero membros: igrejaId=[{}], anoFim=[{}], possuiResultado=[{}]",
                idExternoIgreja, anoFim, response != null);

        return response;
    }
}
