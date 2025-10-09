package com.diacono.diacono.evento.model.entity;

import com.diacono.diacono.global.entity.Igreja;

import com.diacono.diacono.global.util.IdEntityUtils;
import com.diacono.diacono.membro.model.entity.Membro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evento extends IdEntityUtils {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "fk_igreja", nullable = false)
    private Igreja igreja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_organizador",
            nullable = false
    )
    private Membro organizador;

    @OneToMany(
            mappedBy = "evento",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Recorrencia> recorrencias;



}
