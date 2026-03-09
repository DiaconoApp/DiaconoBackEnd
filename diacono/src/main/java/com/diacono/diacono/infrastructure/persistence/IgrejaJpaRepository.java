package com.diacono.diacono.infrastructure.persistence;

import com.diacono.diacono.domain.entity.Igreja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IgrejaJpaRepository extends JpaRepository<Igreja, Long> {

    Igreja findByIdExterno(UUID idExterno);

}
