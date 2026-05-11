package com.diacono.diacono.infrastructure.persistence.gateway;

import com.diacono.diacono.domain.repository.GoogleRefreshTokenMembroRepository;
import com.diacono.diacono.domain.entity.GoogleRefreshTokenMembro;
import com.diacono.diacono.global.util.SensitiveSearchIndexUtils;
import com.diacono.diacono.infrastructure.persistence.springdata.GoogleRefreshTokenMembroJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class GoogleRefreshTokenMembroRepositoryImpl implements GoogleRefreshTokenMembroRepository {

    private final GoogleRefreshTokenMembroJpaRepository jpaRepository;

    public GoogleRefreshTokenMembroRepositoryImpl(GoogleRefreshTokenMembroJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public GoogleRefreshTokenMembro save(GoogleRefreshTokenMembro googleRefreshTokenMembro) {
        return jpaRepository.save(googleRefreshTokenMembro);
    }

    @Override
    public Optional<GoogleRefreshTokenMembro> findByMembroId(UUID membroId) {
        return Optional.ofNullable(jpaRepository.findByMembroId(membroId));
    }

    @Override
    public Optional<GoogleRefreshTokenMembro> findByEmail(String email) {
        return Optional.ofNullable(jpaRepository.findByEmailHash(SensitiveSearchIndexUtils.exactHash(email)));
    }
}


