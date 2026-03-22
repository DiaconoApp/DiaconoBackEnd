package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.MembroMinisterioRepository;
import com.diacono.diacono.presentation.dto.response.MinisterioDashQuantidadeMembrosDTO;

import java.util.List;
import java.util.UUID;

import static com.diacono.diacono.application.validators.DateValidator.validarAnoInicioEFim;

@Service
public class BuscarMinisterioDashMembrosUseCase {

    private final MembroMinisterioRepository repository;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public BuscarMinisterioDashMembrosUseCase(MembroMinisterioRepository repository, JwtClaimsExtractor jwtClaimsExtractor) {
        this.repository = repository;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    public List<MinisterioDashQuantidadeMembrosDTO> ministerioBuscarDashQuantidadeMembro(int anoInicio, int anoFim) {

        validarAnoInicioEFim(anoInicio, anoFim);

        UUID igrejaId = jwtClaimsExtractor.getIgrejaId();

        List<MinisterioDashQuantidadeMembrosDTO> response = repository.buscarQuantidadeMembros(anoInicio, anoFim, igrejaId);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;

    }

}
