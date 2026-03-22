package com.diacono.diacono.application.service;

import com.diacono.diacono.application.mappers.EnderecoEventoMapper;
import com.diacono.diacono.domain.entities.EnderecoEvento;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.persistence.EnderecoEventoRepository;
import com.diacono.diacono.presentation.dto.request.EnderecoEventoDTO;

import java.util.UUID;

public class EnderecoEventoFetcher {

    private final EnderecoEventoRepository repository;
    private final EnderecoEventoMapper mapper;

    public EnderecoEventoFetcher(EnderecoEventoRepository repository, EnderecoEventoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public EnderecoEvento buscarPorUUID(UUID idExterno){
        EnderecoEvento enderecoEvento = repository.findByIdExterno(idExterno);

        if(enderecoEvento == null){
            throw new ObjectNotFoundException("Endereço do evento não encontrado");
        }

        return enderecoEvento;
    }

    public EnderecoEvento converterDtoToEndereco(EnderecoEventoDTO enderecoEventoDTO){
        return mapper.paraEndereco(enderecoEventoDTO);
    }

}
