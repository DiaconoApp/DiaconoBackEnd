package com.diacono.diacono.Igreja.service;

import com.diacono.diacono.Igreja.mapper.IgrejaMapper;
import com.diacono.diacono.Igreja.model.dto.response.IgrejaSemiCompletoDTO;
import com.diacono.diacono.Igreja.model.entity.Igreja;
import com.diacono.diacono.Igreja.repository.IgrejaRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class IgrejaService {

    // OWASP A05: logging seguro sem expor dados sensiveis.
    private static final Logger logger = LoggerFactory.getLogger(IgrejaService.class);

    private final IgrejaRepository igrejaRepository;
    private final IgrejaMapper igrejaMapper;

    public IgrejaService(IgrejaRepository igrejaRepository, IgrejaMapper igrejaMapper) {
        this.igrejaRepository = igrejaRepository;
        this.igrejaMapper = igrejaMapper;
    }

    /* ESSE METODO SE RELACIONA COM EVENTO */
    @Transactional(readOnly = true)
    public Igreja buscarUUID(UUID idExterno) {
        // OWASP A01: validacao defensiva de identificador vindo da camada superior.
        if (idExterno == null) {
            logger.warn("Tentativa de busca de igreja com UUID nulo");
            throw new ObjectNotFoundException("Igreja nao encontrada");
        }

        Igreja igreja = igrejaRepository.findByIdExterno(idExterno);
        if (igreja == null) {
            logger.warn("Igreja nao encontrada para o UUID informado");
            throw new ObjectNotFoundException("Igreja nao encontrada");
        }

        return igreja;
    }

    @Transactional(readOnly = true)
    public List<IgrejaSemiCompletoDTO> buscarIgrejas() {
        List<Igreja> igrejas = igrejaRepository.findAll();

        if (igrejas.isEmpty()) {
            logger.warn("Nenhuma igreja encontrada para listagem");
            throw new ObjectNotFoundException("Igrejas nao encontradas");
        }

        // OWASP A05: fail-safe se o mapeamento retornar vazio/inconsistente.
        List<IgrejaSemiCompletoDTO> resposta = igrejaMapper.paraListaIgrejaSemiCompletoDTO(igrejas);
        if (resposta == null || resposta.isEmpty()) {
            logger.warn("Falha de consistencia ao mapear igrejas para DTO");
            throw new ObjectNotFoundException("Igrejas nao encontradas");
        }

        return resposta;
    }
}
