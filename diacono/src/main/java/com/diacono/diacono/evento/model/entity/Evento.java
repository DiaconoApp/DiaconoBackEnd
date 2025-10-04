package com.diacono.diacono.evento.model.entity;

import com.diacono.diacono.global.entity.Endereco;
import com.diacono.diacono.global.entity.Igreja;

import com.diacono.diacono.global.util.IdEntityUtils;
import com.diacono.diacono.membro.model.entity.Membro;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Entity
@Getter
@Setter
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_endereco", unique = true, nullable = true)
    private Endereco endereco;

    private String nome;
    private String descricao;
    private String publicoAlvo;
    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private BigDecimal custo;

    @Enumerated(EnumType.STRING)
    private TipoRecorrencia tipoRecorrencia;
    private LocalDate dataTerminoRecorrencia;
    @Enumerated(EnumType.STRING)
    private List<DiasSemanaRecorrencia> diasSemana;
    private LocalTime horarioRecorrencia;

}
