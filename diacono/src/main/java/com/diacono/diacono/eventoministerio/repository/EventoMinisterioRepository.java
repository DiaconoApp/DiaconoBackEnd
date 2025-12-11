//package com.diacono.diacono.eventoministerio.repository;
//
//import com.diacono.diacono.evento.model.entity.Evento;
//import com.diacono.diacono.eventoministerio.model.dto.response.EventoMinisterioEscalaDTO;
//
//import com.diacono.diacono.eventoministerio.model.entity.EventoMinisterio;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.UUID;
//
//@Repository
//public interface EventoMinisterioRepository extends JpaRepository<EventoMinisterio, Long> {
//
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM EventoMinisterio em WHERE em.evento = :evento")
//    Integer deleteByEvento(@Param("evento") Evento evento);
//
//    @Query("""
//            SELECT escala FROM Escala escala
//            WHERE escala.eventoMinisterio.ministerio.idExterno = :ministerioId
//            AND FUNCTION('MONTH', escala.eventoMinisterio.evento.dataHoraInicio) = :mes
//            AND FUNCTION('YEAR', escala.eventoMinisterio.evento.dataHoraInicio) = :ano
//            """)
//    List<EventoMinisterioEscalaDTO> findByEventoMinisterioAndMesAndAno(UUID ministerioId, Integer mes, Integer ano);
//
//    @Query("""
//            SELECT escala FROM Escala escala
//            WHERE
//            FUNCTION('MONTH', escala.eventoMinisterio.evento.dataHoraInicio) = :mes
//            AND FUNCTION('YEAR', escala.eventoMinisterio.evento.dataHoraInicio) = :ano
//            """)
//    List<EventoMinisterioEscalaDTO> findByMesAndAno(Integer mes, Integer ano);
//
//    EventoMinisterio findByIdExterno(UUID idExterno);
//
//
//
//}
