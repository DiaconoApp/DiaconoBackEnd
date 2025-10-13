package com.diacono.diacono.Igreja.service;

import com.diacono.diacono.Igreja.model.entity.Igreja;
import com.diacono.diacono.Igreja.repository.IgrejaRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IgrejaService {

    private final IgrejaRepository igrejaRepository;

    public IgrejaService(IgrejaRepository igrejaRepository) {
        this.igrejaRepository = igrejaRepository;
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
}
