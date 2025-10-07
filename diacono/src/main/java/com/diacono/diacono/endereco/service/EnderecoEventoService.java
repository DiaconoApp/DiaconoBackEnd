package com.diacono.diacono.endereco.service;

import com.diacono.diacono.endereco.model.entity.EnderecoEvento;
import com.diacono.diacono.endereco.repository.EnderecoEventoRepository;
import com.diacono.diacono.evento.model.entity.Evento;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EnderecoEventoService {

    private final EnderecoEventoRepository enderecoEventoRepository;

    public EnderecoEventoService(EnderecoEventoRepository enderecoEventoRepository) {
        this.enderecoEventoRepository = enderecoEventoRepository;
    }

    public EnderecoEvento buscarPorUUID(UUID idExterno){
        //validar se o ID está preenchido
        return enderecoEventoRepository.findByIdExterno(idExterno);
    }
}
