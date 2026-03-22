package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.mappers.IgrejaMapper;
import com.diacono.diacono.domain.entities.Igreja;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.persistence.IgrejaRepository;
import com.diacono.diacono.presentation.dto.response.IgrejaSemiCompletoDTO;

import java.util.List;

@Service
public class BuscarIgrejasUseCase {

    private final IgrejaRepository repository;
    private final IgrejaMapper mapper;

    public BuscarIgrejasUseCase(IgrejaRepository repository, IgrejaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<IgrejaSemiCompletoDTO> execute(){
        List<Igreja> igrejas = repository.findAll();

        if(igrejas == null || igrejas.isEmpty()){
            throw new ObjectNotFoundException("Igrejas não encontradas");
        }

        return mapper.paraListaIgrejaSemiCompletoDTO(igrejas);
    }
}
