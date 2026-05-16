package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.domain.entity.EnderecoEvento;
import com.diacono.diacono.domain.repository.EnderecoEventoRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarEnderecoEventoPorUUIDUseCase {

    private static final Logger logger = LoggerFactory.getLogger(BuscarEnderecoEventoPorUUIDUseCase.class);

    private final EnderecoEventoRepository enderecoEventoRepository;

    public BuscarEnderecoEventoPorUUIDUseCase(EnderecoEventoRepository enderecoEventoRepository) {
        this.enderecoEventoRepository = enderecoEventoRepository;
    }

    public EnderecoEvento execute(UUID idExterno){
        if (idExterno == null) {
            logger.warn("Tentativa de buscar endereco de evento sem idExterno informado");
            throw new FieldInvalidException("Id do endereço do evento é obrigatório");
        }

        return enderecoEventoRepository.findByIdExterno(idExterno)
            .orElseThrow(() -> {
                logger.warn("Endereco de evento nao encontrado para idExterno={}", idExterno);
                return new ObjectNotFoundException("Endereço do evento não encontrado");
            });
    }

}