package com.diacono.diacono.infrastructure.persistence.EnderecoEvento;

import com.diacono.diacono.domain.entity.EnderecoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface EnderecoEventoJpaRepository extends JpaRepository<EnderecoEvento, Long> {

    EnderecoEvento findByIdExterno(UUID idExterno);

    @Query("""
    SELECT e FROM EnderecoEvento e 
    WHERE (e.cep = :cep) AND (e.numero = :numero) 
    """)
    EnderecoEvento findByCep(@Param("cep") String cep, @Param("numero") String numero);

}
