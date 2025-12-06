package com.diacono.diacono.evento.repository;

import com.diacono.diacono.evento.model.dto.response.EventoComEventoMinisterioDTO;
import com.diacono.diacono.evento.model.entity.Evento;
import com.diacono.diacono.evento.model.entity.Recorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    @Query("""
    SELECT e FROM Evento e 
    WHERE (e.dataHoraInicio BETWEEN :dataInicio AND :dataFim) AND (e.igreja.idExterno = :igrejaFk)
    ORDER BY e.dataHoraInicio ASC
    """)
    ArrayList<Evento> findByPeriodo(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim, @Param("igrejaFk") UUID igrejaFk);


   Evento findByIdExterno(UUID idExterno);

    @Transactional
    long deleteByIdExterno(UUID idExterno);

    @Query("""
    SELECT e FROM Evento e 
    WHERE (e.recorrencia = :recorrencia) AND (e.dataHoraInicio >= :dataInicio) AND  (e.igreja.idExterno = :igrejaFk)
    ORDER BY e.dataHoraInicio ASC
    """)
    List<Evento> findByPeriodoAndRecorrencia(@Param("recorrencia") Recorrencia recorrencia, @Param("dataInicio") LocalDateTime dataInicio, @Param("igrejaFk") UUID igrejaFk);

    @Query ("""
    SELECT new com.diacono.diacono.evento.model.dto.response.EventoComEventoMinisterioDTO(
        e.idExterno,
        e.nome,
        e.dataHoraFim,
        e.dataHoraInicio,
        COUNT(em.idExterno),
        SUM(CASE WHEN em.isConfirmado = true THEN 1 ELSE 0 END)
    )
    FROM Evento e
    LEFT JOIN com.diacono.diacono.eventoministerio.model.entity.EventoMinisterio em ON em.evento.idExterno = e.idExterno
    WHERE MONTH(e.dataHoraInicio) = :mes AND YEAR(e.dataHoraInicio) = :ano 
    AND e.igreja.idExterno = :igrejaFk
    GROUP BY e.idExterno, e.nome, e.dataHoraFim, e.dataHoraInicio
    ORDER BY e.dataHoraInicio ASC
    """)
    List<EventoComEventoMinisterioDTO> findEventosComEventoMinisterioPorMesAno (
            @Param("mes") int mes,
            @Param("ano") int ano,
            @Param("igrejaFk") UUID igrejaFk
    );
}
