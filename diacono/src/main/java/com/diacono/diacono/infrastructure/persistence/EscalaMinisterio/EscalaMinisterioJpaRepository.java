package com.diacono.diacono.infrastructure.persistence.EscalaMinisterio;

import com.diacono.diacono.domain.entity.EscalaMinisterio;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.infrastructure.persistence.dtos.EscalaMembroMinisterioQueryResult;
import com.diacono.diacono.infrastructure.persistence.dtos.EscalaMinisterioConsolidadoQueryResult;
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
            SELECT new com.diacono.diacono.infrastructure.persistence.dtos.EscalaMinisterioConsolidadoQueryResult(
                e.idExterno,
                e.nome,
                e.dataHoraFim,
                e.dataHoraInicio,
                cast(count(em) as integer),
                cast(sum(case when em.statusEscalaMinisterio = com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio.CONFIRMADO then 1 else 0 end) as integer),
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
              AND (:status IS NULL OR em.statusEscalaMinisterio = :status)
            GROUP BY e.idExterno, e.nome, e.dataHoraFim, e.dataHoraInicio, em.statusEscalaMinisterio
            ORDER BY e.dataHoraInicio ASC
            """)
    List<EscalaMinisterioConsolidadoQueryResult> findEscalaMinisterioConsolidadoByPeriodo(
            @Param("igrejaId") UUID igrejaId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("status") EnumStatusEscalaMinisterio status,
            @Param("listaMinisterios") List<UUID> listaMinisterios,
            @Param("nomeEvento") String nomeEvento
    );

    @Query("""
            SELECT new com.diacono.diacono.infrastructure.persistence.dtos.EscalaMinisterioQueryResult(
                ee.idExterno,
                e.nome,
                mi.nome,
                e.dataHoraFim,
                e.dataHoraInicio,
                em.statusEscalaMinisterio
            )
            FROM EscalaMinisterio em
            JOIN em.escalaEvento ee
            JOIN ee.evento e
            JOIN em.membroMinisterio mm
            JOIN mm.ministerio mi
            WHERE e.dataHoraInicio BETWEEN :dataInicio AND :dataFim
              AND e.igreja.idExterno = :igrejaId
              AND mm.membro.idExterno = :membroId
              AND (:ministerioId IS NULL OR mm.ministerio.idExterno = :ministerioId)
              AND (:nomeEvento IS NULL OR LOWER(e.nome) LIKE CONCAT('%', LOWER(:nomeEvento), '%'))
              AND (:status IS NULL OR em.statusEscalaMinisterio = :status)
            ORDER BY e.dataHoraInicio ASC
            """)
    List<EscalaMinisterioQueryResult> findEscalaMinisterioByPeriodo(
            @Param("igrejaId") UUID igrejaId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("status") EnumStatusEscalaMinisterio status,
            @Param("membroId") UUID membroId,
            @Param("ministerioId") UUID ministerioId,
            @Param("nomeEvento") String nomeEvento
    );

    @Query("""
            SELECT new com.diacono.diacono.infrastructure.persistence.dtos.EscalaMembroMinisterioQueryResult(
                mm.idExterno,
                m.nome,
                em.statusEscalaMinisterio,
                null
            )
            FROM EscalaEvento ee
            JOIN ee.evento e
            JOIN ee.ministerio mi
            JOIN mi.membros mm
            JOIN mm.membro m
            LEFT JOIN EscalaMinisterio em
                   ON em.escalaEvento = ee
                  AND em.membroMinisterio = mm
            WHERE e.igreja.idExterno = :igrejaId
              AND ee.idExterno = :escalaEventoId
            ORDER BY m.nome ASC
            """)
    List<EscalaMembroMinisterioQueryResult> findEscalaMembroMinisterioByEscalaEventoId(
            @Param("igrejaId") UUID igrejaId,
            @Param("escalaEventoId") UUID escalaEventoId
    );
}

