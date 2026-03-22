package com.diacono.diacono.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Evento extends IdEntityUtils {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "fk_igreja", nullable = false)
    private Igreja igreja;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_organizador",
            nullable = false
    )
    private Membro organizador;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fk_endereco", unique = false, nullable = true)
    private EnderecoEvento enderecoEvento;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "evento_ministerio",
            joinColumns = @JoinColumn(name = "fk_evento"),
            inverseJoinColumns = @JoinColumn(name = "fk_ministerio")
    )
    private Set<Ministerio> ministerios;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
            name = "fk_recorrencia",
            nullable = true,
            unique = false
    )
    private Recorrencia recorrencia;

    private String nome;
    private String descricao;
    private String publicoAlvo;
    @Column(name = "data_hora_inicio")
    private LocalDateTime dataHoraInicio;
    @Column(name = "data_hora_fim")
    private LocalDateTime dataHoraFim;
    private BigDecimal custo;

    public void setIgreja(Igreja igreja) {
        this.igreja = igreja;
    }

    public void setOrganizador(Membro organizador) {
        this.organizador = organizador;
    }

    public void setEnderecoEvento(EnderecoEvento enderecoEvento) {
        this.enderecoEvento = enderecoEvento;
    }

    public void setMinisterios(Set<Ministerio> ministerios) {
        this.ministerios = ministerios;
    }

    public void setRecorrencia(Recorrencia recorrencia) {
        this.recorrencia = recorrencia;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPublicoAlvo(String publicoAlvo) {
        this.publicoAlvo = publicoAlvo;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public void setCusto(BigDecimal custo) {
        this.custo = custo;
    }
}
