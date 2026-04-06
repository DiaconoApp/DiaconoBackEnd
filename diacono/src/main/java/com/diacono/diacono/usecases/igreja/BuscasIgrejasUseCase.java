package com.diacono.diacono.usecases.igreja;

import com.diacono.diacono.applications.dtos.igreja.IgrejaSemiCompletoDTO;
import com.diacono.diacono.applications.mappers.igreja.IgrejaMapper;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.repository.IgrejaRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscasIgrejasUseCase {

    private final IgrejaRepository igrejaRepository;
    private final IgrejaMapper igrejaMapper;

    public BuscasIgrejasUseCase(IgrejaRepository igrejaRepository, IgrejaMapper igrejaMapper) {
        this.igrejaRepository = igrejaRepository;
        this.igrejaMapper = igrejaMapper;
    }

    public List<IgrejaSemiCompletoDTO> execute(){

        List<IgrejaSemiCompletoDTO> igrejas = buscarIgrejas();

        return igrejas;
    }

    public List<IgrejaSemiCompletoDTO> buscarIgrejas(){

        List<Igreja> igrejas = igrejaRepository.findAll();

        if(igrejas == null || igrejas.isEmpty()){
            throw new ObjectNotFoundException("Igrejas não encontradas");
        }

        return igrejaMapper.paraListaIgrejaSemiCompletoDTO(igrejas);
    }
}
