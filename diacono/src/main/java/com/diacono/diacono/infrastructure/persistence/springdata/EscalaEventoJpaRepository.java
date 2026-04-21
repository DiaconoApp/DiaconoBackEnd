package com.diacono.diacono.infrastructure.persistence.springdata;

import com.diacono.diacono.domain.entity.EscalaEvento;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.infrastructure.persistence.projection.EscalaEventoEscaladoQueryResult;
import com.diacono.diacono.infrastructure.persistence.projection.EscalaEventoQueryResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EscalaEventoJpaRepository extends JpaRepository<EscalaEvento, Long> {
    @Query("""
            SELECT new com.diacono.diacono.infrastructure.persistence.projection.EscalaEventoQueryResult(
                e.idExterno,
                e.nome,
                e.dataHoraFim,
                e.dataHoraInicio,
                cast(count(ee) as integer),
                cast(sum(case when ee.statusEscalaEvento = com.diacono.diacono.domain.enums.EnumStatusEvento.CONFIRMADO then 1 else 0 end) as integer),
                e.status
            )
            FROM EscalaEvento ee
            JOIN ee.evento e
            WHERE (e.dataHoraInicio BETWEEN :dataInicio AND :dataFim)
              AND (e.igreja.idExterno = :igrejaFk)
              AND (:status IS NULL OR e.status = :status)
              AND (:ministerioId IS NULL OR ee.ministerio.idExterno = :ministerioId)
              AND (:nomeEvento IS NULL OR LOWER(e.nome) LIKE CONCAT('%', LOWER(:nomeEvento), '%'))
            GROUP BY e.idExterno, e.nome, e.dataHoraFim, e.dataHoraInicio, e.status
            ORDER BY e.dataHoraInicio ASC
            """)
    List<EscalaEventoQueryResult> findEscalaEventoConsolidadoByPeriodo(@Param("igrejaFk") UUID igrejaFk,
                                                                        @Param("dataInicio") LocalDateTime dataInicio,
                                                                        @Param("dataFim") LocalDateTime dataFim,
                                                                        @Param("status") EnumStatusEvento status,
                                                                        @Param("ministerioId") UUID ministerioId,
                                                                        @Param("nomeEvento") String nomeEvento);

     @Query("""
             SELECT new com.diacono.diacono.infrastructure.persistence.projection.EscalaEventoEscaladoQueryResult(
                 m.idExterno,
                 m.nome,
                 ee.idExterno,
                 CASE WHEN ee.idExterno IS NOT NULL THEN true ELSE false END
             )
             FROM Ministerio m
             LEFT JOIN EscalaEvento ee ON ee.ministerio.idInterno = m.idInterno AND ee.evento.idExterno = :eventoId
             WHERE m.igreja.idExterno = :igrejaFk
             ORDER BY CASE WHEN ee.idExterno IS NOT NULL THEN 0 ELSE 1 END, m.nome ASC
             """)
     List<EscalaEventoEscaladoQueryResult> findEscalaEventoEscaladoByEventoId(@Param("igrejaFk") UUID igrejaFk,
                                                                               @Param("eventoId") UUID eventoId);

     @Query("""
             SELECT ee.ministerio.idExterno
             FROM EscalaEvento ee
             JOIN ee.evento e
             WHERE e.igreja.idExterno = :idIgreja
               AND ee.idExterno = :escalaEventoId
             """)
    UUID findMinisterioIdByEscalaEventoId(@Param("idIgreja") UUID idIgreja,
                                          @Param("escalaEventoId") UUID escalaEventoId);

    @Query("""
            SELECT ee
            FROM EscalaEvento ee
            JOIN ee.evento e
            WHERE e.igreja.idExterno = :igrejaId
              AND ee.idExterno = :escalaEventoId
            """)
    EscalaEvento findEscalaEventoByIdExternoAndIgrejaId(
            @Param("igrejaId") UUID igrejaId,
            @Param("escalaEventoId") UUID escalaEventoId
    );

    @Query("""
            SELECT ee.evento.idExterno
            FROM EscalaEvento ee
            WHERE ee.idExterno = :escalaEventoId
            """)
    UUID findEventoIdByEscalaEventoId(@Param("escalaEventoId") UUID escalaEventoId);

    @Query("""
            SELECT CASE
                WHEN COUNT(ee) > 0 AND SUM(CASE WHEN ee.statusEscalaEvento = com.diacono.diacono.domain.enums.EnumStatusEvento.CONFIRMADO THEN 1 ELSE 0 END) = COUNT(ee)
                THEN true
                ELSE false
            END
            FROM EscalaEvento ee
            WHERE ee.evento.idExterno = :eventoId
            """)
    boolean areAllConfirmadosByEventoId(@Param("eventoId") UUID eventoId);

    @Modifying
    @Query("""
            UPDATE EscalaEvento ee
            SET ee.statusEscalaEvento = :status
            WHERE ee.idExterno = :escalaEventoId
              AND ee.statusEscalaEvento <> :status
            """)
    void updateStatusByEscalaEventoId(@Param("escalaEventoId") UUID escalaEventoId, @Param("status") EnumStatusEvento status);
}
