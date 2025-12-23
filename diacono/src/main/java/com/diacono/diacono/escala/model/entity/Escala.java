package com.diacono.diacono.escala.model.entity;

import com.diacono.diacono.global.util.IdEntityUtils;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Escala extends IdEntityUtils {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_ministerio", nullable = false)
    private Ministerio ministerio;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "evento_ministerio",
            joinColumns = @JoinColumn(name = "fk_escala"),
            inverseJoinColumns = @JoinColumn(name = "fk_ministerio")
    )
    private Set<Membro> membros;

}
