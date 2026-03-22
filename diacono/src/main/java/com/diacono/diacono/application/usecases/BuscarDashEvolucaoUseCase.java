package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.MembroRepository;
import com.diacono.diacono.presentation.dto.response.MembroDashEvolucaoDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.diacono.diacono.application.validators.DateValidator.validarAnoInicioEFim;

@Service
public class BuscarDashEvolucaoUseCase {

    private final MembroRepository repository;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public BuscarDashEvolucaoUseCase(MembroRepository repository, JwtClaimsExtractor jwtClaimsExtractor) {
        this.repository = repository;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    public List<MembroDashEvolucaoDTO> buscarDashEvolucao(int anoInicio, int anoFim) {

        validarAnoInicioEFim(anoInicio, anoFim);

        UUID idExternoIgreja = jwtClaimsExtractor.getIgrejaId();

        List<MembroDashEvolucaoDTO> bruto = repository.buscarMembrosPorAno(idExternoIgreja, anoInicio, anoFim);

        if (bruto == null || bruto.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return bruto;
    }

}
