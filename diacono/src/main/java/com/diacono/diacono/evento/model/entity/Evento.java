package com.diacono.diacono.evento.model.entity;

import com.diacono.diacono.endereco.model.entity.EnderecoEvento;
import com.diacono.diacono.Igreja.model.entity.Igreja;

import com.diacono.diacono.global.util.IdEntityUtils;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_endereco", unique = false, nullable = true)
    private EnderecoEvento enderecoEvento;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "evento_ministerio",
            joinColumns = @JoinColumn(name = "fk_evento"),
            inverseJoinColumns = @JoinColumn(name = "fk_ministerio")
    )
    private Set<Ministerio> ministerios;

    private String nome;
    private String descricao;
    private String publicoAlvo;
    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private BigDecimal custo;

    @Enumerated(EnumType.STRING)
    private TipoRecorrencia tipoRecorrencia;
    private LocalDate dataInicioRecorrencia;
    private LocalDate dataTerminoRecorrencia;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<DiasSemanaRecorrencia> diasSemana;
    private int intervaloRecorrencia;


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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPublicoAlvo(String publicoAlvo) {
        this.publicoAlvo = publicoAlvo;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public void setCusto(BigDecimal custo) {
        this.custo = custo;
    }

    public void setTipoRecorrencia(TipoRecorrencia tipoRecorrencia) {
        this.tipoRecorrencia = tipoRecorrencia;
    }

    public void setDataInicioRecorrencia(LocalDate dataInicioRecorrencia) {
        this.dataInicioRecorrencia = dataInicioRecorrencia;
    }

    public void setDataTerminoRecorrencia(LocalDate dataTerminoRecorrencia) {
        this.dataTerminoRecorrencia = dataTerminoRecorrencia;
    }

    public void setDiasSemana(List<DiasSemanaRecorrencia> diasSemana) {
        this.diasSemana = diasSemana;
    }

    public void setIntervaloRecorrencia(int intervaloRecorrencia) {
        this.intervaloRecorrencia = intervaloRecorrencia;
    }
}
