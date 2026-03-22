package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.mappers.MinisterioMapper;
import com.diacono.diacono.domain.entities.Ministerio;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.MinisteriosRepository;
import com.diacono.diacono.presentation.dto.MinisterioSimplificadoDTO;

@Service
public class BuscarMinisterioComFiltroUseCase {

    private final MinisteriosRepository repository;
    private final JwtClaimsExtractor jwtClaimsExtractor;
    private final MinisterioMapper mapper;

    public BuscarMinisterioComFiltroUseCase(MinisteriosRepository repository, JwtClaimsExtractor jwtClaimsExtractor, MinisterioMapper mapper) {
        this.repository = repository;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
        this.mapper = mapper;
    }

    public Page<MinisterioSimplificadoDTO> buscarMinisteriosGovernoComFiltro(Pageable pageable, String buscaGeral, EnumStatusMinisterio status) {

        String stringBusca = "%" + buscaGeral.trim().toUpperCase() + "%";

        Page<Ministerio> ministeriosPage = repository.buscarComFiltros(pageable, stringBusca, status, jwtClaimsExtractor.getIgrejaId());
        if (ministeriosPage.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum ministério encontrado");
        }

        //mapper::paraMinisterioSimplificadoDTO == (m -> mapper.paraMinisterioSimplificadoDTO(m))
        Page<MinisterioSimplificadoDTO> responses = ministeriosPage
                .map(mapper::paraMinisterioSimplificadoDTO);

        return responses;

    }

}
