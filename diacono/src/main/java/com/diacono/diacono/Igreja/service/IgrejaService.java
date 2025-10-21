package com.diacono.diacono.Igreja.service;

import com.diacono.diacono.Igreja.mapper.IgrejaMapper;
import com.diacono.diacono.Igreja.model.dto.response.IgrejaSemiCompletoDTO;
import com.diacono.diacono.Igreja.model.entity.Igreja;
import com.diacono.diacono.Igreja.repository.IgrejaRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IgrejaService {

    private final IgrejaRepository igrejaRepository;
    private final IgrejaMapper igrejaMapper;

    public IgrejaService(IgrejaRepository igrejaRepository, IgrejaMapper igrejaMapper) {
        this.igrejaRepository = igrejaRepository;
        this.igrejaMapper = igrejaMapper;
    }

    /*ESSE MÉTODO SE RELACIONA COM EVENTO*/
    public Igreja buscarUUID(UUID idExterno){
        //adicionar validação da existência da Igreja -- SE DER ERRO LANÇAR EXCEÇÃO
        Igreja igreja = igrejaRepository.findByIdExterno(idExterno);

        if(igreja == null){
            throw new ObjectNotFoundException("Igreja não encontrada");
        }

        return igreja;

    }

    public List<IgrejaSemiCompletoDTO> buscarIgrejas(){

        List<Igreja> igrejas = igrejaRepository.findAll();

        if(igrejas == null || igrejas.isEmpty()){
            throw new ObjectNotFoundException("Igrejas não encontradas");
        }

        return igrejaMapper.paraListaIgrejaSemiCompletoDTO(igrejas);

    }
}
