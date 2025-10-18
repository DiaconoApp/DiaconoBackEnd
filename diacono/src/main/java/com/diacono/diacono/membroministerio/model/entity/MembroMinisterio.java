package com.diacono.diacono.membroministerio.model.entity;

import com.diacono.diacono.global.util.IdEntityUtils;
import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "membro_ministerio", uniqueConstraints = @UniqueConstraint(columnNames = {"fk_membro", "fk_ministerio"}))
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Setter
@Getter
public class MembroMinisterio extends IdEntityUtils{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_membro", nullable = false)
    private Membro membro;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_ministerio", nullable = false)
    private Ministerio ministerio;

    @Enumerated(EnumType.STRING)
    @Column(name = "cargo_membro", nullable = false)
    private EnumCargoMembroMinisterio cargoMembro = EnumCargoMembroMinisterio.MEMBRO_MINISTERIO;

    @Column(name="nome_ministerio")
    private String nomeMinisterio;
}
