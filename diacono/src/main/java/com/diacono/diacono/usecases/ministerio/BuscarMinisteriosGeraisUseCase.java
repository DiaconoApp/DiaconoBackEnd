package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.applications.mappers.ministerio.MinisterioMapper;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarMinisteriosGeraisUseCase {

    private static final Logger logger = LoggerFactory.getLogger(BuscarMinisteriosGeraisUseCase.class);

    private final MinisteriosRepository ministeriosRepository;
    private final MinisterioMapper ministerioMapper;

    public BuscarMinisteriosGeraisUseCase(
            MinisteriosRepository ministeriosRepository,
            MinisterioMapper ministerioMapper
    ) {
        this.ministeriosRepository = ministeriosRepository;
        this.ministerioMapper = ministerioMapper;
    }

    @Transactional(readOnly = true)
    public List<MinisterioSimplificadoDTO> execute(UUID igrejaId) {

        List<Ministerio> ministerios = ministeriosRepository.findByIgrejaIdExterno(igrejaId);

        if (ministerios.isEmpty()) {
            logger.warn("Nenhum ministério encontrado para a igreja do token. igrejaId=[{}]", igrejaId);
            throw new ObjectNotFoundException("Nenhum ministério encontrado");
        }

        return ministerios.stream()
                .map(ministerioMapper::paraMinisterioSimplificadoDTO)
                .toList();
    }
}