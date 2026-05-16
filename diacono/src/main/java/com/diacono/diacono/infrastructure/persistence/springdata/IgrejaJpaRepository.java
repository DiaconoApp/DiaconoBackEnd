package com.diacono.diacono.infrastructure.persistence.springdata;

import com.diacono.diacono.domain.entity.Igreja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IgrejaJpaRepository extends JpaRepository<Igreja, Long> {

    Optional<Igreja> findByIdExterno(UUID idExterno);
}
