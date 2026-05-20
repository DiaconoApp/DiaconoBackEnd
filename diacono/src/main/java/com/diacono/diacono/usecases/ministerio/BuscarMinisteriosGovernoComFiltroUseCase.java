package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.applications.mappers.ministerio.MinisterioMapper;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BuscarMinisteriosGovernoComFiltroUseCase {

    private static final Logger logger = LoggerFactory.getLogger(BuscarMinisteriosGovernoComFiltroUseCase.class);

    private final MinisteriosRepository ministeriosRepository;
    private final MinisterioMapper mapper;

    public BuscarMinisteriosGovernoComFiltroUseCase(MinisteriosRepository ministeriosRepository, MinisterioMapper mapper) {
        this.ministeriosRepository = ministeriosRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Page<MinisterioSimplificadoDTO> execute(Pageable pageable, String buscaGeral, EnumStatusMinisterio status, UUID igrejaId) {

        String stringBusca = normalizarBusca(buscaGeral);

        Page<Ministerio> ministeriosPage = ministeriosRepository.buscarComFiltros(pageable, stringBusca, status, igrejaId);

        logger.info("Busca de ministérios com filtro: igrejaId=[{}], possuiBusca=[{}], status=[{}], total=[{}]",
                igrejaId, stringBusca != null, status, ministeriosPage.getTotalElements());

        return ministeriosPage.map(mapper::paraMinisterioSimplificadoDTO);
    }

    private String normalizarBusca(String buscaGeral) {
        if (buscaGeral == null) {
            return null;
        }

        String buscaNormalizada = buscaGeral.trim().toUpperCase();
        if (buscaNormalizada.isBlank()) {
            return null;
        }

        return buscaNormalizada;
    }
}
