package com.diacono.diacono.usecases.dashboard.membros;

import com.diacono.diacono.applications.dtos.dashboard.DashboardFaixaEtariaMembroDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDashFaixaEtariaDTO;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarKpiFaixaEtariaMembrosDashUseCase {

    private final DashboardPeriodoValidator periodoValidator;
    private final MembroRepository membroRepository;
    private final JwtUtils jwtUtils;

    public BuscarKpiFaixaEtariaMembrosDashUseCase(
            DashboardPeriodoValidator periodoValidator,
            MembroRepository membroRepository,
            JwtUtils jwtUtils
    ) {
        this.periodoValidator = periodoValidator;
        this.membroRepository = membroRepository;
        this.jwtUtils = jwtUtils;
    }

    public DashboardFaixaEtariaMembroDTO execute(int anoInicio, int anoFim) {
        periodoValidator.validarAnoInicioEFim(anoInicio, anoFim);

        MembroDashFaixaEtariaDTO faixaEtaria = buscarDashFaixaEtaria(anoFim);

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

    public MembroDashFaixaEtariaDTO buscarDashFaixaEtaria(int anoFim) {

        UUID idExternoIgreja = jwtUtils.getIgrejaId();

        MembroDashFaixaEtariaDTO response = membroRepository.buscarMembrosPorFaixaEtaria(idExternoIgreja, anoFim);


        return response;
    }
}