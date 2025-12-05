package com.diacono.diacono.escala.repository;

import com.diacono.diacono.escala.model.dto.response.EscalaMembroDTO;
import com.diacono.diacono.escala.model.entity.Escala;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EscalaRepository extends JpaRepository<Escala, Long> {

    @Query("""
            SELECT escala FROM Escala escala
            WHERE escala.eventoMinisterio.id = :membroMinisterioId
            """)
    List<EscalaMembroDTO> findByEventoMinisterioId(@Param("membroMinisterioId") UUID membroMinisterioId);

    @Query("""
            SELECT escala FROM Escala escala
            WHERE escala.membroMinisterio.id = :membroMinisterioId
            AND escala.eventoMinisterio.evento.dataHoraInicio < :horarioFim
            AND escala.eventoMinisterio.evento.dataHoraFim > :horarioInicio
            """)
    List<EscalaMembroDTO> findEscalaConflitante(@Param("membroMinisterioId") Long membroMinisterioId,
                                       @Param("horarioInicio") LocalDateTime horarioInicio,
                                       @Param("horarioFim") LocalDateTime horarioFim);

    @Query("""
            SELECT escala FROM Escala escala
            WHERE escala.membroMinisterio.idExterno = :membroMinisterioId
            AND FUNCTION('MONTH', escala.eventoMinisterio.evento.dataHoraInicio) = :mes
            AND FUNCTION('YEAR', escala.eventoMinisterio.evento.dataHoraInicio) = :ano
            """)
    List<EscalaMembroDTO> findByMembroMinisterioIdAndMesAndAno(@Param("membroMinisterioId") UUID membroMinisterioId,
                                                      @Param("mes") Integer mes,
                                                      @Param("ano") Integer ano);

    @Query("""
            DELETE FROM Escala escala
            WHERE escala.eventoMinisterio.id = :eventoMinisterioId
            """)
    Integer deleteByEventoMinisterioId(@Param("eventoMinisterioId") UUID eventoMinisterioId);

    @Query("""
            SELECT new com.diacono.diacono.escala.model.dto.response.EscalaMembroDTO(
                escala.eventoMinisterio.evento.nome,
                escala.eventoMinisterio.evento.dataHoraInicio,
                escala.eventoMinisterio.evento.dataHoraFim,
                escala.membroMinisterio.ministerio.nome
            )
            FROM Escala escala
            WHERE escala.membroMinisterio.idExterno = :membroId
            AND FUNCTION('MONTH', escala.eventoMinisterio.evento.dataHoraInicio) = :mes
            AND FUNCTION('YEAR', escala.eventoMinisterio.evento.dataHoraInicio) = :ano
            """)
    List<EscalaMembroDTO> findByMembroIdAndMesAndAno(@Param("membroId") UUID idExternoMembroMinisterio,
                                                     @Param("mes") Integer mes,
                                                     @Param("ano") Integer ano);

}
