package com.diacono.diacono.evento.repository;

import com.diacono.diacono.evento.model.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    @Query("SELECT e FROM Evento e WHERE e.data BETWEEN :inicio AND :fim")
    List<Evento> findByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT COUNT(e) FROM Evento e WHERE e.data BETWEEN :inicio AND :fim")
    int countEventosNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);


}
