package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.applications.mappers.ministerio.MinisterioMapper;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BuscarMinisteriosGovernoSemFiltroUseCase {

    private static final Logger logger = LoggerFactory.getLogger(BuscarMinisteriosGovernoSemFiltroUseCase.class);

    private final MinisteriosRepository ministeriosRepository;
    private final MinisterioMapper ministerioMapper;

    public BuscarMinisteriosGovernoSemFiltroUseCase(
            MinisteriosRepository ministeriosRepository,
            MinisterioMapper ministerioMapper
    ) {
        this.ministeriosRepository = ministeriosRepository;
        this.ministerioMapper = ministerioMapper;
    }

    @Transactional(readOnly = true)
    public Page<MinisterioSimplificadoDTO> execute(Pageable pageable, UUID igrejaId) {

        Page<Ministerio> ministeriosPage = ministeriosRepository.findByIgrejaIdExterno(igrejaId, pageable);

        if (ministeriosPage.isEmpty()) {
            logger.warn("Nenhum ministério encontrado para a igreja do token. igrejaId=[{}]", igrejaId);
            throw new ObjectNotFoundException("Nenhum ministério encontrado");
        }

        return ministeriosPage.map(ministerioMapper::paraMinisterioSimplificadoDTO);
    }

}