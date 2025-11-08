package com.diacono.diacono.evento.repository;

import com.diacono.diacono.evento.model.entity.Evento;
import com.diacono.diacono.evento.model.entity.Recorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    @Query("""
    SELECT e FROM Evento e 
    WHERE (e.data BETWEEN :inicio AND :fim)
    ORDER BY e.data ASC
    """)
    ArrayList<Evento> findByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT COUNT(e) FROM Evento e WHERE e.data BETWEEN :inicio AND :fim")
    int countEventosNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

   Evento findByIdExterno(UUID idExterno);

    @Transactional
    long deleteByIdExterno(UUID idExterno);

    @Query("""
    SELECT e FROM Evento e 
    WHERE (e.recorrencia = :recorrencia) AND (e.data >= :data) 
    ORDER BY e.data ASC
    """)
    List<Evento> findByPeriodoAndRecorrencia(@Param("recorrencia") Recorrencia recorrencia, @Param("data") LocalDate data);
}
