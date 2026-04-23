package com.diacono.diacono.domain.entity;

import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.global.util.IdEntityUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Getter
@Setter
public class Ministerio extends IdEntityUtils {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_igreja")
    private Igreja igreja;

    @Column(length = 100, nullable = false)
    private String nome;

    private LocalDate dataCriacao;

    @Column(length = 150)
    private String nomeLider;

    @Enumerated(EnumType.STRING)
    private EnumStatusMinisterio status;

    @OneToMany(mappedBy = "ministerio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MembroMinisterio> membros = new HashSet<>();

    @OneToMany(mappedBy = "ministerio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EscalaEvento> escalasEvento = new HashSet<>();
}