package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.EventoRepository;
import com.diacono.diacono.presentation.dto.response.MinisterioEventoDashDTO;

import java.util.List;
import java.util.UUID;

import static com.diacono.diacono.application.validators.DateValidator.validarAnoInicioEFim;

@Service
public class BuscarMinisterioDashEventosUseCase {

    private final EventoRepository repository;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public BuscarMinisterioDashEventosUseCase(EventoRepository repository, JwtClaimsExtractor jwtClaimsExtractor) {
        this.repository = repository;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    public List<MinisterioEventoDashDTO> ministerioBuscarDashQuantidadeEventos(int anoInicio, int anoFim) {

        validarAnoInicioEFim(anoInicio, anoFim);

        UUID idIgreja = jwtClaimsExtractor.getIgrejaId();

        List<MinisterioEventoDashDTO> response = repository.contarEventosPorMinisterioNoPeriodo(anoInicio, anoFim, idIgreja);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;

    }

}
