package com.diacono.diacono.infrastructure.persistence.EscalaMinisterio;

import com.diacono.diacono.domain.entity.EscalaMinisterio;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.infrastructure.persistence.dtos.EscalaMembroMinisterioQueryResult;
import com.diacono.diacono.infrastructure.persistence.dtos.EscalaMinisterioConsolidadoQueryResult;
import com.diacono.diacono.infrastructure.persistence.dtos.EscalaMinisterioQueryResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
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
                ee.idExterno,
                e.nome,
                m.nome,
                e.dataHoraFim,
                e.dataHoraInicio,
                cast(count(em) as integer),
                cast(sum(case when em.statusEscalaMinisterio = com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio.CONFIRMADO then 1 else 0 end) as integer),
                case
                    when em.idInterno is null then com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio.PENDENTE
                    else em.statusEscalaMinisterio
                end
            )
            FROM EscalaEvento ee
            LEFT JOIN EscalaMinisterio em ON em.escalaEvento = ee
            JOIN ee.evento e
                JOIN ee.ministerio m
            WHERE e.dataHoraInicio BETWEEN :dataInicio AND :dataFim
              AND e.igreja.idExterno = :igrejaId
              AND (:listaMinisterios IS NULL OR ee.ministerio.idExterno IN :listaMinisterios)
              AND (:nomeEvento IS NULL OR LOWER(e.nome) LIKE CONCAT('%', LOWER(:nomeEvento), '%'))
              AND (
                    :status IS NULL
                    OR em.statusEscalaMinisterio = :status
                    OR (:status = com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio.PENDENTE AND em.idInterno IS NULL)
                  )
            GROUP BY ee.idExterno, e.idExterno, m.idExterno, e.nome, m.nome, e.dataHoraFim, e.dataHoraInicio,
                     case
                         when em.idInterno is null then com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio.PENDENTE
                         else em.statusEscalaMinisterio
                     end
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
                e.idExterno,
                em.idExterno,
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

    @Query("""
            SELECT DISTINCT mm.idExterno
            FROM EscalaEvento ee
            JOIN ee.evento e
            JOIN ee.ministerio mi
            JOIN mi.membros mm
            WHERE e.igreja.idExterno = :igrejaId
              AND ee.idExterno = :escalaEventoId
              AND EXISTS (
                  SELECT 1
                  FROM EscalaMinisterio emConflito
                  JOIN emConflito.escalaEvento eeConflito
                  JOIN eeConflito.evento eConflito
                  JOIN emConflito.membroMinisterio mmConflito
                  WHERE mmConflito.membro.idExterno = mm.membro.idExterno
                    AND eConflito.igreja.idExterno = :igrejaId
                    AND (
                        (
                            eeConflito.idExterno = ee.idExterno
                            AND eeConflito.ministerio.idExterno <> ee.ministerio.idExterno
                        )
                        OR (
                            eeConflito.idExterno <> ee.idExterno
                            AND eConflito.dataHoraInicio <= e.dataHoraFim
                            AND eConflito.dataHoraFim >= e.dataHoraInicio
                        )
                    )
              )
            """)
    List<UUID> findMembrosMinisterioOcupadosByEscalaEventoId(
            @Param("igrejaId") UUID igrejaId,
            @Param("escalaEventoId") UUID escalaEventoId
    );

    @Modifying
    @Query("""
            DELETE FROM EscalaMinisterio em
            WHERE em.escalaEvento.idExterno = :escalaEventoId
              AND em.escalaEvento.evento.igreja.idExterno = :igrejaId
            """)
    void deleteByEscalaEventoIdAndIgrejaId(
            @Param("igrejaId") UUID igrejaId,
            @Param("escalaEventoId") UUID escalaEventoId
    );



    @Query("""
            SELECT mm
            FROM EscalaEvento ee
            JOIN ee.ministerio mi
            JOIN mi.membros mm
            JOIN mm.membro m
            WHERE ee.idExterno = :escalaEventoId
              AND m.igreja.idExterno = :igrejaId
              AND mm.idExterno IN :membrosMinisterioIds
            """)
    List<MembroMinisterio> findMembrosMinisterioByEscalaEventoIdAndIds(
            @Param("igrejaId") UUID igrejaId,
            @Param("escalaEventoId") UUID escalaEventoId,
            @Param("membrosMinisterioIds") List<UUID> membrosMinisterioIds
    );

    @Query("""
            SELECT CASE
                WHEN COUNT(em) > 0 AND SUM(CASE WHEN em.statusEscalaMinisterio = com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio.CONFIRMADO THEN 1 ELSE 0 END) = COUNT(em)
                THEN true
                ELSE false
            END
            FROM EscalaMinisterio em
            WHERE em.escalaEvento.idExterno = :escalaEventoId
            """)
    boolean areAllConfirmadosByEscalaEventoId(@Param("escalaEventoId") UUID escalaEventoId);

}

