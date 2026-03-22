//package com.diacono.diacono.escala.model.entity;
//
//import com.diacono.diacono.eventoministerio.model.entity.EventoMinisterio;
//import com.diacono.diacono.domain.entities.IdEntityUtils;
//import com.diacono.diacono.domain.entities.MembroMinisterio;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Entity
//@Table(name = "escala", uniqueConstraints = @UniqueConstraint(columnNames = {"fk_evento_ministerio", "fk_membro_ministerio"}))
//@Setter
//@Getter
//public class Escala extends IdEntityUtils {
//
//    @ManyToOne
//    @JoinColumn(name = "fk_evento_ministerio", nullable = false)
//    private EventoMinisterio eventoMinisterio;
//
//    @ManyToOne
//    @JoinColumn(name = "fk_membro_ministerio", nullable = false)
//    private MembroMinisterio membroMinisterio;
//}
