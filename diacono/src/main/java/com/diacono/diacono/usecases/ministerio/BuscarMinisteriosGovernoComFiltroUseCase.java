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
public class BuscarMinisteriosGovernoComFiltroUseCase {

    private final MinisteriosRepository ministeriosRepository;
    private final MinisterioMapper mapper;
    private final JwtUtils jwtUtils;

    public BuscarMinisteriosGovernoComFiltroUseCase(MinisteriosRepository ministeriosRepository, MinisterioMapper mapper, JwtUtils jwtUtils) {
        this.ministeriosRepository = ministeriosRepository;
        this.mapper = mapper;
        this.jwtUtils = jwtUtils;
    }

    @Transactional(readOnly = true)
    public Page<MinisterioSimplificadoDTO> execute(Pageable pageable, String buscaGeral, EnumStatusMinisterio status) {

        String stringBusca = "%" + buscaGeral.trim().toUpperCase() + "%";

        Page<Ministerio> ministeriosPage = ministeriosRepository.buscarComFiltros(pageable, stringBusca, status, jwtUtils.getIgrejaId());
        if (ministeriosPage.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum ministério encontrado");
        }

        return ministeriosPage.map(mapper::paraMinisterioSimplificadoDTO);
    }
}
