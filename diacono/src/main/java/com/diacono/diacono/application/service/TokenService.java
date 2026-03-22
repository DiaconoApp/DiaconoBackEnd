package com.diacono.diacono.application.service;

import com.diacono.diacono.domain.entities.Membro;

public interface TokenService {

    String generateToken(Membro membro);
    long expiresIn();
}
