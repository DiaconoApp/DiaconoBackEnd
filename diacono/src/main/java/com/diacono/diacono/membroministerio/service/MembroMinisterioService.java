package com.diacono.diacono.membroministerio.service;

import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import com.diacono.diacono.membroministerio.repository.MembroMinisterioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MembroMinisterioService {

    private final MembroMinisterioRepository membroMinisterioRepository;

    public MembroMinisterioService(MembroMinisterioRepository membroMinisterioRepository) {
        this.membroMinisterioRepository = membroMinisterioRepository;
    }

    public void apagarMembroMinisterioPorMembro(Membro membro){
        int count = membroMinisterioRepository.deleteByMembro(membro);

    }

    public void salvarTodos(List<MembroMinisterio> membrosMinisterios){
        List<MembroMinisterio> membroMinisterios = membroMinisterioRepository.saveAll(membrosMinisterios);

        if(membroMinisterios.isEmpty()){
            throw new ObjectSaveErrorException("Nenhum membro_ministerio foi salvo.");
        }


    }

    // Metodo usado na escala service
    public List<MembroMinisterio> buscarMembroMinisterioPorId(List<UUID> idsExternoMembroMinisterio) {
        List<MembroMinisterio> membrosMinisterio = membroMinisterioRepository
                .findAllByIdExternoIn(idsExternoMembroMinisterio);

        if(membrosMinisterio.isEmpty()){
            throw new ObjectNotFoundException("Nenhum membro_ministerio encontrado para os IDs fornecidos.");
        }

        return membrosMinisterio;
    }

}
