package com.diacono.diacono.infrastructure.persistence.EscalaMinisterio;

import com.diacono.diacono.domain.entity.EscalaMinisterio;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.infrastructure.persistence.dtos.EscalaMinisterioQueryResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EscalaMinisterioJpaRepository extends JpaRepository<EscalaMinisterio, Long> {

    @Query("""
            SELECT new com.diacono.diacono.infrastructure.persistence.dtos.EscalaMinisterioQueryResult(
                e.idExterno,
                e.nome,
                e.dataHoraFim,
                e.dataHoraInicio,
                cast(count(em) as integer),
                cast(sum(case when em.membroConfirmado = true then 1 else 0 end) as integer),
                em.statusEscalaMinisterio
            )
            FROM EscalaMinisterio em
            JOIN em.escalaEvento ee
            JOIN ee.evento e
            JOIN em.membroMinisterio mm
            WHERE e.dataHoraInicio BETWEEN :dataInicio AND :dataFim
              AND e.igreja.idExterno = :igrejaId
              AND (:listaMinisterios IS NULL OR mm.ministerio.idExterno IN :listaMinisterios)
              AND (:nomeEvento IS NULL OR LOWER(e.nome) LIKE CONCAT('%', LOWER(:nomeEvento), '%'))
            GROUP BY e.idExterno, e.nome, e.dataHoraFim, e.dataHoraInicio, em.statusEscalaMinisterio
            HAVING (:status IS NULL OR em.statusEscalaMinisterio = :status)
            ORDER BY e.dataHoraInicio ASC
            """)
    List<EscalaMinisterioQueryResult> findEscalaMinisterioConsolidadoByPeriodo(
            @Param("igrejaId") UUID igrejaId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("status") EnumStatusEscalaMinisterio status,
            @Param("listaMinisterios") List<UUID> listaMinisterios,
            @Param("nomeEvento") String nomeEvento
    );
}

