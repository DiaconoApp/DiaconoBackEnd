package com.diacono.diacono.infrastructure.persistence.gateway;

import com.diacono.diacono.domain.entity.EnderecoEvento;
import com.diacono.diacono.domain.repository.EnderecoEventoRepository;
import com.diacono.diacono.infrastructure.persistence.springdata.EnderecoEventoJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EnderecoEventoRepositoryImpl implements EnderecoEventoRepository {

    private final EnderecoEventoJpaRepository jpaRepository;

    public EnderecoEventoRepositoryImpl(EnderecoEventoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<EnderecoEvento> findByIdExterno(UUID idExterno) {
        return Optional.ofNullable(jpaRepository.findByIdExterno(idExterno));
    }

    @Override
    public Optional<EnderecoEvento> findByCep(String cep, String numero) {
        return Optional.ofNullable(jpaRepository.findByCep(cep, numero));
    }

    @Override
    public EnderecoEvento save(EnderecoEvento enderecoEvento) {
        return jpaRepository.save(enderecoEvento);
    }
}