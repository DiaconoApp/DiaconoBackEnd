package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.mappers.MinisterioMapper;
import com.diacono.diacono.domain.entities.Ministerio;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.MinisteriosRepository;
import com.diacono.diacono.presentation.dto.MinisterioSimplificadoDTO;

import java.util.List;

@Service
public class BuscarMinisteriosUseCase {

    private final MinisteriosRepository repository;
    private final JwtClaimsExtractor jwtClaimsExtractor;
    private final MinisterioMapper mapper;

    public BuscarMinisteriosUseCase(MinisteriosRepository repository, JwtClaimsExtractor jwtClaimsExtractor, MinisterioMapper mapper) {
        this.repository = repository;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
        this.mapper = mapper;
    }

    public List<MinisterioSimplificadoDTO> buscarMinisteriosGerais() {

        List<Ministerio> ministeriosResponse = repository.findByIgreja_IdExterno(jwtClaimsExtractor.getIgrejaId());
        if (ministeriosResponse.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum ministério encontrado");

        }

        List<MinisterioSimplificadoDTO> responses = ministeriosResponse.stream()
                .map(mapper::paraMinisterioSimplificadoDTO)
                .toList();

        return responses;

    }
}
