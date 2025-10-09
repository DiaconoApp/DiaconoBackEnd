package com.diacono.diacono.evento.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Recorrencia {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_evento", nullable = false)
    private Evento evento;
}
