package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.applications.mappers.ministerio.MinisterioMapper;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class BuscarMinisteriosGovernoComFiltroUseCase {

    private final MinisteriosRepository ministeriosRepository;
    private final MinisterioMapper mapper;
    private final JwtUtils jwtUtils;

    public BuscarMinisteriosGovernoComFiltroUseCase(MinisteriosRepository ministeriosRepository, MinisterioMapper mapper, JwtUtils jwtUtils) {
        this.ministeriosRepository = ministeriosRepository;
        this.mapper = mapper;
        this.jwtUtils = jwtUtils;
    }

    public Page<MinisterioSimplificadoDTO> execute(Pageable pageable, String buscaGeral, EnumStatusMinisterio status) {

        String stringBusca = "%" + buscaGeral.trim().toUpperCase() + "%";

        Page<Ministerio> ministeriosPage = ministeriosRepository.buscarComFiltros(pageable, stringBusca, status, jwtUtils.getIgrejaId());
        if (ministeriosPage.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum ministério encontrado");
        }

        //mapper::paraMinisterioSimplificadoDTO == (m -> mapper.paraMinisterioSimplificadoDTO(m))
        Page<MinisterioSimplificadoDTO> responses = ministeriosPage
                .map(mapper::paraMinisterioSimplificadoDTO);

        return responses;
    }
}
