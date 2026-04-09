package com.diacono.diacono.infrastructure.persistence.Igreja;

import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.repository.IgrejaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class IgrejaRepositoryImpl implements IgrejaRepository {

    private final IgrejaJpaRepository igrejaJpaRepository;

    public IgrejaRepositoryImpl(IgrejaJpaRepository igrejaJpaRepository) {
        this.igrejaJpaRepository = igrejaJpaRepository;
    }

    @Override
    public Optional<Igreja> findByIdExterno(UUID idExterno) {
        return igrejaJpaRepository.findByIdExterno(idExterno);
    }

    @Override
    public List<Igreja> findAll() {
        return igrejaJpaRepository.findAll();
    }
}