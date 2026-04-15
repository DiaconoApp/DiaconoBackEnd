package com.diacono.diacono.domain.entity;

import com.diacono.diacono.global.util.IdEntityUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "escala_evento", uniqueConstraints = @UniqueConstraint(columnNames = {"fk_evento", "fk_ministerio"}))
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Setter
@Getter
public class EscalaEvento extends IdEntityUtils {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_evento", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_ministerio", nullable = false)
    private Ministerio ministerio;

    private Boolean ministerioConfirmado = true;
}

