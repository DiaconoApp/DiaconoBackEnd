package com.diacono.diacono.membro.model.entity;

import com.diacono.diacono.Igreja.model.entity.Igreja;
import com.diacono.diacono.global.util.IdEntityUtils;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Membro extends IdEntityUtils {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "fk_igreja", nullable = false)
    private Igreja igreja;

    private String nome;

    private String cpf;

    private LocalDate dataNascimento;

    private String email;

    private String celular;

    private String senha;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "membro_ministerio",
            joinColumns = @JoinColumn(name = "fk_membro"),
            inverseJoinColumns = @JoinColumn(name = "fk_ministerio")
    )
    private List<Ministerio> ministerio;

    @Enumerated(EnumType.STRING)
    private EnumStatusMembro statusMembro = EnumStatusMembro.ATIVO;

//    @OneToMany(mappedBy = "membro", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<MembroMinisterio> ministerios;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id")
    private EnderecoMembro enderecoMembro;

    @ManyToOne
    @JoinColumn(name = "discipulador_id")
    private Membro discipulador;
}
