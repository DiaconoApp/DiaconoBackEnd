package com.diacono.diacono.infrastructure.persistence.springdata;

import com.diacono.diacono.domain.entity.GoogleRefreshTokenMembro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GoogleRefreshTokenMembroJpaRepository extends JpaRepository<GoogleRefreshTokenMembro, Long> {
    @Deprecated
    GoogleRefreshTokenMembro findByMembroId(UUID membroId);
    @Deprecated
    GoogleRefreshTokenMembro findByEmailHash(String emailHash);
    Optional<GoogleRefreshTokenMembro> findByMembroIdAndIgrejaId(UUID membroId, UUID igrejaId);
    Optional<GoogleRefreshTokenMembro> findByEmailHashAndIgrejaId(String emailHash, UUID igrejaId);
}
