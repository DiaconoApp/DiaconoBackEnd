package com.diacono.diacono.domain.repository;

import com.diacono.diacono.domain.entity.Igreja;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IgrejaRepository {
    Optional<Igreja> findByIdExterno(UUID idExterno);
    List<Igreja> findAll();
}