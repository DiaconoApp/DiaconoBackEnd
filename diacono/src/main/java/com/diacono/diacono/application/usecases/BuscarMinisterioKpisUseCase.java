package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.EventoRepository;
import com.diacono.diacono.infrastructure.persistence.MinisteriosRepository;
import com.diacono.diacono.presentation.dto.MinisterioKpisResponseDTO;
import com.diacono.diacono.presentation.dto.response.EventoKpiDTO;
import com.diacono.diacono.presentation.dto.response.KpisMinisteriosDTO;

import java.util.List;
import java.util.UUID;

import static com.diacono.diacono.application.validators.DateValidator.validarAnoInicioEFim;

@Service
public class BuscarMinisterioKpisUseCase {

    private final MinisteriosRepository repository;
    private final JwtClaimsExtractor jwtClaimsExtractor;
    private final EventoRepository eventoRepository;

    public BuscarMinisterioKpisUseCase(MinisteriosRepository repository, JwtClaimsExtractor jwtClaimsExtractor, EventoRepository eventoRepository) {
        this.repository = repository;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
        this.eventoRepository = eventoRepository;
    }

    public KpisMinisteriosDTO ministerioBuscarKpis(int anoInicio, int anoFim) {

        validarAnoInicioEFim(anoInicio, anoFim);

        UUID igrejaId = jwtClaimsExtractor.getIgrejaId();
        MinisterioKpisResponseDTO kpiMinisterio =  repository.buscarKpis(igrejaId, anoFim);

        List<EventoKpiDTO> kpiEvento = eventoRepository.buscarKpisEvento(anoInicio, anoFim, igrejaId);

        if (kpiMinisterio == null || kpiEvento == null || kpiEvento.isEmpty()){
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        EventoKpiDTO eventoRetido = kpiEvento.get(0);


        KpisMinisteriosDTO response = new KpisMinisteriosDTO(
                eventoRetido,
                kpiMinisterio
        );

        return response;
    }

}
