package com.diacono.diacono.evento.repository;

import com.diacono.diacono.evento.model.entity.EnderecoEvento;
import com.diacono.diacono.evento.model.entity.Recorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface EnderecoEventoRepository extends JpaRepository<EnderecoEvento, Long> {
    // OWASP A01: Prefira o findByIdExterno para garantir que a consulta seja sempre filtrada pela igreja, evitando vazamento de dados cross-tenant.
    EnderecoEvento findByIdExterno(UUID idExterno);

    // OWASP A01: query interna com filtro multi-tenant explicito por igreja.
    EnderecoEvento findByIdExternoAndIgrejaIdExterno(UUID idExterno, UUID IdeExternoIgreja);

    // OWASP A01: Prefira o findByCepAndIgrejaIdExterno para garantir que a consulta seja sempre filtrada pela igreja, evitando vazamento de dados cross-tenant.
    @Query("""
    SELECT e FROM EnderecoEvento e 
    WHERE (e.cep = :cep) AND (e.numero = :numero) 
    """)
    EnderecoEvento findByCep(@Param("cep") String cep, @Param("numero") String numero);

    // OWASP A01: consulta sensivel filtrada explicitamente pela igreja autenticada do contexto superior.
    EnderecoEvento findByCepAndIgrejaIdExterno(String cep, UUID igrejaIdExterno);

}
