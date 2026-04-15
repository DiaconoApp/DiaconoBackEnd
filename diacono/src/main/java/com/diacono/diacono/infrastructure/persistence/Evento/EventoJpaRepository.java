package com.diacono.diacono.infrastructure.persistence.Evento;

import com.diacono.diacono.applications.dtos.evento.EventoKpiDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioEventoDashDTO;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.entity.Recorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface  EventoJpaRepository extends JpaRepository<Evento, Long> {

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

//    @Query("""
//            SELECT new com.diacono.diacono.applications.dtos.evento.EventoComEventoMinisterioDTO(
//                e.idExterno,
//                e.nome,
//                e.dataHoraFim,
//                e.dataHoraInicio,
//                COUNT(em.idExterno),
//                SUM(CASE WHEN em.isConfirmado = true THEN 1 ELSE 0 END)
//            )
//            FROM Evento e
//            LEFT JOIN com.diacono.diacono.eventoministerio.model.entity.EventoMinisterio em ON em.evento.idExterno = e.idExterno
//            WHERE MONTH(e.dataHoraInicio) = :mes AND YEAR(e.dataHoraInicio) = :ano
//            AND e.igreja.idExterno = :igrejaFk
//            GROUP BY e.idExterno, e.nome, e.dataHoraFim, e.dataHoraInicio
//            ORDER BY e.dataHoraInicio ASC
//            """)
//    List<EventoComEventoMinisterioDTO> findEventosComEventoMinisterioPorMesAno(
//            @Param("mes") int mes,
//            @Param("ano") int ano,
//            @Param("igrejaFk") UUID igrejaFk
//    );

    //kpis

    @Query("""
            SELECT new com.diacono.diacono.applications.dtos.evento.EventoKpiDTO(
                m.nome,
                COUNT(e.idExterno)
            )
            FROM Evento e
            JOIN e.escalaEvento escala
            JOIN escala.ministerio m
            WHERE FUNCTION('YEAR', e.dataHoraInicio) BETWEEN :anoInicio AND :anoFim
            AND e.igreja.idExterno = :idIgreja
            GROUP BY m.nome
            ORDER BY COUNT(e.idExterno) DESC
            """)
    List<EventoKpiDTO> buscarKpisEvento(@Param("anoInicio") int anoInicio, @Param("anoFim") int anoFim, @Param("idIgreja") UUID idIgreja);

    @Query("""
            SELECT new com.diacono.diacono.applications.dtos.ministerio.MinisterioEventoDashDTO(
                min.nome,
                COUNT(e)
            )
            FROM Evento e
            JOIN e.escalaEvento escala
            JOIN escala.ministerio min
            WHERE e.igreja.idExterno = :idIgreja  
            AND FUNCTION('YEAR', e.dataHoraInicio) BETWEEN :anoInicio and :anoFim
            GROUP BY min.nome
            ORDER BY COUNT(e) DESC
            """)
    List<MinisterioEventoDashDTO> contarEventosPorMinisterioNoPeriodo(@Param("anoInicio") int anoInicio, @Param("anoFim") int anoFim, @Param("idIgreja") UUID idIgreja);
}
