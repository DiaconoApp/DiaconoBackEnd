package com.diacono.diacono.domain.repository;

import com.diacono.diacono.domain.entity.GoogleRefreshTokenMembro;

import java.util.Optional;
import java.util.UUID;

public interface GoogleRefreshTokenMembroRepository {
    GoogleRefreshTokenMembro save(GoogleRefreshTokenMembro googleRefreshTokenMembro);
    Optional<GoogleRefreshTokenMembro> findByMembroId(UUID membroId);
    Optional<GoogleRefreshTokenMembro> findByEmail(String email);
}


