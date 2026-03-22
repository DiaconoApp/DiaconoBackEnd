package com.diacono.diacono.infrastructure.persistence;

import com.diacono.diacono.domain.entities.Recorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecorrenciaRepository extends JpaRepository<Recorrencia, Long> {

}
