package com.diacono.diacono.eventoministerio.model.entity;

import com.diacono.diacono.evento.model.entity.Evento;
import com.diacono.diacono.global.util.IdEntityUtils;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "evento_ministerio")
@Setter
@Getter
public class EventoMinisterio extends IdEntityUtils {

    @ManyToOne
    @JoinColumn(name = "fk_evento", nullable = false)
    @NotNull
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "fk_ministerio", nullable = false)
    @NotNull
    private Ministerio ministerio;

    @NotNull
    private Boolean isConfirmado;

    public EventoMinisterio() {
        this.isConfirmado = false;
    }
}
