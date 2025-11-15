package com.diacono.diacono.ministerio.model.entity;

import com.diacono.diacono.global.util.IdEntityUtils;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import jakarta.persistence.*;
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
public class Ministerio extends IdEntityUtils{


    private String nome;
    private LocalDate dataCriacao;
    private String nomeLider;
    @Enumerated(EnumType.STRING)
    private EnumStatusMinisterio status;
    @OneToMany(mappedBy = "ministerio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MembroMinisterio> membros = new HashSet<>();


}

