package com.diacono.diacono.evento.repository;

import com.diacono.diacono.evento.model.dto.response.EventoKpiDTO;
import com.diacono.diacono.evento.model.dto.response.MinisterioEventoDashDTO;
import com.diacono.diacono.evento.model.entity.Evento;
import com.diacono.diacono.evento.model.entity.Recorrencia;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    // OWASP A01: consulta sensivel filtrada explicitamente pela igreja autenticada do contexto superior.
    @Query("""
            SELECT e FROM Evento e
            WHERE (e.dataHoraInicio BETWEEN :dataInicio AND :dataFim) AND (e.igreja.idExterno = :igrejaFk)
            ORDER BY e.dataHoraInicio ASC
            """)
    ArrayList<Evento> findByPeriodo(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim, @Param("igrejaFk") UUID igrejaFk);

    // OWASP A01: Prefira o findByIgrejaIdExternoAndEventoIdExterno para garantir que a consulta seja sempre filtrada pela igreja, evitando vazamento de dados cross-tenant.
    @Deprecated
    Evento findByIdExterno(UUID idExterno);

    // OWASP A01: query interna com filtro multi-tenant explicito por igreja.
    @Query("""
            SELECT e FROM Evento e
            WHERE e.igreja.idExterno = :igrejaFk AND e.idExterno = :idExterno
            """)
    Evento findByIgrejaIdExternoAndEventoIdExterno(@Param("igrejaFk") UUID igrejaFk, @Param("idExterno") UUID idExterno);

    // OWASP A01: Prefira o deleteByIgrejaIdExternoAndEventoIdExterno para garantir que a delecao seja sempre filtrada pela igreja, evitando vazamento de dados cross-tenant.
    @Deprecated
    @Transactional
    long deleteByIdExterno(UUID idExterno);

    // OWASP A01: query interna de delecao com filtro multi-tenant explicito por igreja.
    //@Transactional
    //@Query("""
    //        DELETE FROM Evento e
    //        WHERE e.igreja.idExterno = :igrejaFk AND e.idExterno = :idExterno
    //        """)
    //long deleteByIgrejaIdExternoAndEventoIdExterno(@Param("igrejaFk") UUID igrejaFk, @Param("idExterno") UUID idExterno);

    // OWASP A01: consulta de recorrencia ja restringida pela igreja.
    @Query("""
            SELECT e FROM Evento e
            WHERE (e.recorrencia = :recorrencia) AND (e.dataHoraInicio >= :dataInicio) AND  (e.igreja.idExterno = :igrejaFk)
            ORDER BY e.dataHoraInicio ASC
            """)
    List<Evento> findByPeriodoAndRecorrencia(@Param("recorrencia") Recorrencia recorrencia, @Param("dataInicio") LocalDateTime dataInicio, @Param("igrejaFk") UUID igrejaFk);

//    @Query("""
//            SELECT new com.diacono.diacono.evento.model.dto.response.EventoComEventoMinisterioDTO(
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

    // OWASP A01: KPI sensivel agregado por igreja, evitando mistura de dados entre tenants.
    @Query("""
            SELECT new com.diacono.diacono.evento.model.dto.response.EventoKpiDTO(
                m.nome,
                COUNT(e.idExterno)
            )
            FROM Evento e
            JOIN e.ministerios m
            WHERE YEAR(e.dataHoraInicio) BETWEEN :anoInicio AND :anoFim
            AND e.igreja.idExterno = :idIgreja
            GROUP BY m.nome
            ORDER BY COUNT(e.idExterno) DESC
            """)
    List<EventoKpiDTO> buscarKpisEvento(@Param("anoInicio") int anoInicio, @Param("anoFim") int anoFim, @Param("idIgreja") UUID idIgreja);

    // OWASP A01: dashboard agregado restrito ao tenant da igreja informada.
    @Query("""
            SELECT new com.diacono.diacono.evento.model.dto.response.MinisterioEventoDashDTO(
                min.nome,
                COUNT(e)
            )
            FROM Evento e
            JOIN e.ministerios min
            WHERE e.igreja.idExterno = :idIgreja
            AND FUNCTION('YEAR', e.dataHoraInicio) BETWEEN :anoInicio and :anoFim
            GROUP BY min.nome
            ORDER BY COUNT(e) DESC
            """)
    List<MinisterioEventoDashDTO> contarEventosPorMinisterioNoPeriodo(@Param("anoInicio") int anoInicio, @Param("anoFim") int anoFim, @Param("idIgreja") UUID idIgreja);
}



