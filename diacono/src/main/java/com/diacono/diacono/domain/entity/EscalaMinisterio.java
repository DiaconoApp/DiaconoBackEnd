package com.diacono.diacono.domain.entity;

import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.global.util.IdEntityUtils;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "escala_ministerio", uniqueConstraints = @UniqueConstraint(columnNames = {"fk_escala_evento", "fk_membro_ministerio"}))
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Setter
@Getter
public class EscalaMinisterio extends IdEntityUtils {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_escala_evento", nullable = false)
    private EscalaEvento escalaEvento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_membro_ministerio", nullable = false)
    private MembroMinisterio membroMinisterio;

    @Builder.Default
    private Boolean membroConfirmado = true;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EnumStatusEscalaMinisterio statusEscalaMinisterio = EnumStatusEscalaMinisterio.CONFIRMADO;
}
