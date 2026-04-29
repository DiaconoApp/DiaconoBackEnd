package com.diacono.diacono.infrastructure.persistence.springdata;

import com.diacono.diacono.domain.entity.GoogleRefreshTokenMembro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GoogleRefreshTokenMembroJpaRepository extends JpaRepository<GoogleRefreshTokenMembro, Long> {
    GoogleRefreshTokenMembro findByMembroId(UUID membroId);
    GoogleRefreshTokenMembro findByEmail(String email);
}

