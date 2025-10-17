package com.diacono.diacono.membroministerio.service;

import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import com.diacono.diacono.membroministerio.repository.MembroMinisterioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembroMinisterioService {

    private final MembroMinisterioRepository membroMinisterioRepository;

    public MembroMinisterioService(MembroMinisterioRepository membroMinisterioRepository) {
        this.membroMinisterioRepository = membroMinisterioRepository;
    }

    public void apagarMembroMinisterioPorMembro(Membro membro){
        int count = membroMinisterioRepository.deleteByMembro(membro);

        if(count == 0){
            throw new ObjectNotFoundException("Nenhum membro_ministerio encontrado para o membro.");
        }

    }

    public void salvarTodos(List<MembroMinisterio> membrosMinisterios){
        List<MembroMinisterio> membroMinisterios = membroMinisterioRepository.saveAll(membrosMinisterios);

        if(membroMinisterios.isEmpty()){
            throw new ObjectSaveErrorException("Nenhum membro_ministerio foi salvo.");
        }


    }

}
