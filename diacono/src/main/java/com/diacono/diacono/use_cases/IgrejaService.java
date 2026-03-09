package com.diacono.diacono.use_cases;

import com.diacono.diacono.applications.mappers.igreja.IgrejaMapper;
import com.diacono.diacono.applications.dtos.igreja.IgrejaSemiCompletoDTO;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.infrastructure.persistence.IgrejaJpaRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IgrejaService {

    private final IgrejaJpaRepository igrejaJpaRepository;
    private final IgrejaMapper igrejaMapper;

    public IgrejaService(IgrejaJpaRepository igrejaJpaRepository, IgrejaMapper igrejaMapper) {
        this.igrejaJpaRepository = igrejaJpaRepository;
        this.igrejaMapper = igrejaMapper;
    }

    /*ESSE MÉTODO SE RELACIONA COM EVENTO*/
    public Igreja buscarUUID(UUID idExterno){
        //adicionar validação da existência da Igreja -- SE DER ERRO LANÇAR EXCEÇÃO
        Igreja igreja = igrejaJpaRepository.findByIdExterno(idExterno);
        if(igreja == null){
            throw new ObjectNotFoundException("Igreja não encontrada");
        }

        return igreja;

    }

    public List<IgrejaSemiCompletoDTO> buscarIgrejas(){

        List<Igreja> igrejas = igrejaJpaRepository.findAll();

        if(igrejas == null || igrejas.isEmpty()){
            throw new ObjectNotFoundException("Igrejas não encontradas");
        }

        return igrejaMapper.paraListaIgrejaSemiCompletoDTO(igrejas);

    }
}
