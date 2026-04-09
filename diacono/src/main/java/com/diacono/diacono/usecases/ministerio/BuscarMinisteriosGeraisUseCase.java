package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.applications.mappers.ministerio.MinisterioMapper;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BuscarMinisteriosGeraisUseCase {

    private final MinisteriosRepository ministeriosRepository;
    private final MinisterioMapper ministerioMapper;
    private final JwtUtils jwtUtils;

    public BuscarMinisteriosGeraisUseCase(
            MinisteriosRepository ministeriosRepository,
            MinisterioMapper ministerioMapper,
            JwtUtils jwtUtils
    ) {
        this.ministeriosRepository = ministeriosRepository;
        this.ministerioMapper = ministerioMapper;
        this.jwtUtils = jwtUtils;
    }

    @Transactional(readOnly = true)
    public List<MinisterioSimplificadoDTO> execute() {

        List<Ministerio> ministerios = ministeriosRepository.findByIgrejaIdExterno(jwtUtils.getIgrejaId());

        if (ministerios.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum ministério encontrado");
        }

        return ministerios.stream()
                .map(ministerioMapper::paraMinisterioSimplificadoDTO)
                .toList();
    }
}