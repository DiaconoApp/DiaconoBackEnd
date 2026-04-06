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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuscarMinisteriosGovernoSemFiltroUseCase {

    private final MinisteriosRepository ministeriosRepository;
    private final MinisterioMapper ministerioMapper;
    private final JwtUtils jwtUtils;

    public BuscarMinisteriosGovernoSemFiltroUseCase(
            MinisteriosRepository ministeriosRepository,
            MinisterioMapper ministerioMapper,
            JwtUtils jwtUtils
    ) {
        this.ministeriosRepository = ministeriosRepository;
        this.ministerioMapper = ministerioMapper;
        this.jwtUtils = jwtUtils;
    }

    public Page<MinisterioSimplificadoDTO> execute(Pageable pageable) {

        Page<Ministerio> ministeriosPage = ministeriosRepository.findByIgrejaIdExterno(jwtUtils.getIgrejaId(), pageable);

        if (ministeriosPage.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum ministério encontrado");
        }

        //mapper::paraMinisterioSimplificadoDTO == (m -> mapper.paraMinisterioSimplificadoDTO(m))

        return ministeriosPage.map(ministerioMapper::paraMinisterioSimplificadoDTO);
    }

}