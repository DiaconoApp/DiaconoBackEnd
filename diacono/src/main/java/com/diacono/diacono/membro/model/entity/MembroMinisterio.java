package com.diacono.diacono.membro.model.entity;

import com.diacono.diacono.global.util.IdEntityUtils;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "membro_ministerio", uniqueConstraints = @UniqueConstraint(columnNames = {"membro_id", "ministerio_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
@SuperBuilder(toBuilder = true)
public class MembroMinisterio  extends IdEntityUtils {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "membro_id", nullable = false)
    private Membro membro;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ministerio_id", nullable = false)
    private Ministerio ministerio;

    @Enumerated(EnumType.STRING)
    @Column(name = "cargo_membro", nullable = false)
    private EnumCargoMembro cargoMembro = EnumCargoMembro.MEMBRO;
}
