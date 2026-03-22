package com.diacono.diacono.application.usecases;

import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.MembroRepository;
import com.diacono.diacono.presentation.dto.response.DashboardFaixaEtariaMembroDTO;
import com.diacono.diacono.presentation.dto.response.MembroDashFaixaEtariaDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.diacono.diacono.application.validators.DateValidator.validarAnoInicioEFim;

@Service
public class BuscarDashFaixaEtariaUseCase {

    private final MembroRepository repository;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public BuscarDashFaixaEtariaUseCase(MembroRepository repository, JwtClaimsExtractor jwtClaimsExtractor) {
        this.repository = repository;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    public DashboardFaixaEtariaMembroDTO buscarDashFaixaEtaria(int anoInicio, int anoFim) {

        validarAnoInicioEFim(anoInicio, anoFim);

        UUID idExternoIgreja = jwtClaimsExtractor.getIgrejaId();

        MembroDashFaixaEtariaDTO faixaEtaria = repository.buscarMembrosPorFaixaEtaria(idExternoIgreja, anoFim);

        long total = faixaEtaria.criancas() + faixaEtaria.adolescentes() + faixaEtaria.jovens()
                + faixaEtaria.adultos() + faixaEtaria.idosos();


        if (total == 0) {
            return new DashboardFaixaEtariaMembroDTO(0, 0, 0, 0, 0);
        }

        DashboardFaixaEtariaMembroDTO faixaEtariaDTO = new DashboardFaixaEtariaMembroDTO(
                (faixaEtaria.criancas() * 100) / total,
                (faixaEtaria.adolescentes() * 100) / total,
                (faixaEtaria.jovens() * 100) / total,
                (faixaEtaria.adultos() * 100) / total,
                (faixaEtaria.idosos() * 100) / total
        );

        return faixaEtariaDTO;
    }

}
