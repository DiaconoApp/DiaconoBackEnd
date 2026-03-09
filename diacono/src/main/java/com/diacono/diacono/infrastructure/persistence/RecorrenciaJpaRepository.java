package com.diacono.diacono.infrastructure.persistence;

import com.diacono.diacono.domain.entity.Recorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecorrenciaJpaRepository extends JpaRepository<Recorrencia, Long> {

}
