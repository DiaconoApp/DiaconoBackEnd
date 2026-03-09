package com.diacono.diacono.domain.repository;

import com.diacono.diacono.domain.entity.EnderecoEvento;
import java.util.Optional;
import java.util.UUID;

public interface EnderecoEventoRepository {
    Optional<EnderecoEvento> findByIdExterno(UUID idExterno);
    Optional<EnderecoEvento> findByCep(String cep, String numero);
    EnderecoEvento save(EnderecoEvento enderecoEvento);
}