package com.diacono.diacono.domain.entity;

import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumGeneroMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.global.util.IdEntityUtils;
import com.diacono.diacono.global.util.SensitiveSearchIndexUtils;
import com.diacono.diacono.global.util.SensitiveStringAttributeConverter;
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

    @Convert(converter = SensitiveStringAttributeConverter.class)
    @Column(length = 1024)
    private String nome;

    @Convert(converter = SensitiveStringAttributeConverter.class)
    @Column(length = 1024)
    private String cpf;

    @Column(name = "cpf_hash", length = 32)
    private String cpfHash;

    private LocalDate dataNascimento;

    private LocalDate dataRegistro;

    @Convert(converter = SensitiveStringAttributeConverter.class)
    @Column(length = 1024)
    private String email;

    @Column(name = "email_hash", length = 32)
    private String emailHash;

    @Convert(converter = SensitiveStringAttributeConverter.class)
    @Column(length = 1024)
    private String celular;

    @Column(name = "busca_tokens", length = 65535)
    private String buscaTokens;

    private String senha;

    @Enumerated(EnumType.STRING)
    private EnumStatusMembro status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumCargoMembro cargoMembro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumGeneroMembro generoMembro;

    @PrePersist
    @PreUpdate
    public void atualizarIndicesCamposSensiveis() {
        this.emailHash = SensitiveSearchIndexUtils.exactHash(this.email);
        this.cpfHash = SensitiveSearchIndexUtils.exactHash(this.cpf);
        this.buscaTokens = SensitiveSearchIndexUtils.searchTokens(this.nome, this.email, this.celular);
    }

    public boolean possuiIndicesSensiveis() {
        return emailHash != null && cpfHash != null && buscaTokens != null;
    }

}
