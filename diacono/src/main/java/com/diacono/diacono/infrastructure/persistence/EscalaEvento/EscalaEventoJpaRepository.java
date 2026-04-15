package com.diacono.diacono.infrastructure.persistence.EscalaEvento;

import com.diacono.diacono.domain.entity.EscalaEvento;
import com.diacono.diacono.infrastructure.persistence.dto.EscalaEventoQueryResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EscalaEventoJpaRepository extends JpaRepository<EscalaEvento, Long> {
    @Query("""
            SELECT new com.diacono.diacono.infrastructure.persistence.dto.EscalaEventoQueryResult(
                e.idExterno,
                e.nome,
                e.dataHoraFim,
                e.dataHoraInicio,
                cast(count(ee) as integer),
                cast(sum(case when ee.ministerioConfirmado = true then 1 else 0 end) as integer)
            )
            FROM EscalaEvento ee
            JOIN ee.evento e
            WHERE (e.dataHoraInicio BETWEEN :dataInicio AND :dataFim)
              AND (e.igreja.idExterno = :igrejaFk)
            GROUP BY e.idExterno, e.nome, e.dataHoraFim, e.dataHoraInicio
            ORDER BY e.dataHoraInicio ASC
            """)
    List<EscalaEventoQueryResult> findEscalaEventoByPeriodo(@Param("igrejaFk") UUID igrejaFk, @Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);
}
