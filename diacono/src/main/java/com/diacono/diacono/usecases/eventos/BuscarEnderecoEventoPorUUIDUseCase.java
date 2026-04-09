package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.domain.entity.EnderecoEvento;
import com.diacono.diacono.domain.repository.EnderecoEventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarEnderecoEventoPorUUIDUseCase {

    private final EnderecoEventoRepository enderecoEventoRepository;

    public BuscarEnderecoEventoPorUUIDUseCase(EnderecoEventoRepository enderecoEventoRepository) {
        this.enderecoEventoRepository = enderecoEventoRepository;
    }

    public EnderecoEvento execute(UUID idExterno){
        return enderecoEventoRepository.findByIdExterno(idExterno)
            .orElseThrow(() -> new ObjectNotFoundException("Endereço do evento não encontrado"));
    }

}