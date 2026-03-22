package com.diacono.diacono.domain.entities;

import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumGeneroMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Membro extends IdEntityUtils {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "fk_igreja")
    private Igreja igreja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discipulador_id")
    private Membro discipulador;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_endereco")
    private EnderecoMembro enderecoMembro;

    @OneToMany(mappedBy = "membro", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<MembroMinisterio> ministerios = new HashSet<>();

    private String nome;

    private String cpf;

    private LocalDate dataNascimento;

    private LocalDate dataRegistro;

    private String email;

    private String celular;

    private String senha;

    @Enumerated(EnumType.STRING)
    private EnumStatusMembro status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumCargoMembro cargoMembro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumGeneroMembro generoMembro;

}
