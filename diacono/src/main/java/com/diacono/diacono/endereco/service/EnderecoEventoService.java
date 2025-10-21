package com.diacono.diacono.endereco.service;

import com.diacono.diacono.endereco.model.entity.EnderecoEvento;
import com.diacono.diacono.endereco.repository.EnderecoEventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EnderecoEventoService {

    private final EnderecoEventoRepository enderecoEventoRepository;

    public EnderecoEventoService(EnderecoEventoRepository enderecoEventoRepository) {
        this.enderecoEventoRepository = enderecoEventoRepository;
    }

    public EnderecoEvento buscarPorUUID(UUID idExterno){
        EnderecoEvento enderecoEvento = enderecoEventoRepository.findByIdExterno(idExterno);

        if(enderecoEvento == null){
            throw new ObjectNotFoundException("Endereço do evento não encontrado");
        }

        return enderecoEvento;
    }

    public void salvarEnderecoEvento(EnderecoEvento endereco){
        EnderecoEvento salvo = enderecoEventoRepository.save(endereco);
    }

}
