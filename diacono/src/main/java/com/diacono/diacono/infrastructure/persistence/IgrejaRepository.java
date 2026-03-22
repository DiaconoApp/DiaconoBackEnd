package com.diacono.diacono.infrastructure.persistence;

import com.diacono.diacono.domain.entities.Igreja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IgrejaRepository extends JpaRepository<Igreja, Long> {

    Igreja findByIdExterno(UUID idExterno);

}
