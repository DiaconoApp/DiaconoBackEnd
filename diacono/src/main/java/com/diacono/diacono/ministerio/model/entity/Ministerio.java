package com.diacono.diacono.ministerio.model.entity;

import com.diacono.diacono.global.util.IdEntityUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@SuperBuilder(toBuilder = true)
public class Ministerio extends IdEntityUtils{


    private String nome;
    private LocalDate dataCriacao;
    private String nomeLider;
    private String status;

    public Ministerio(String nome, LocalDate data, String nomeLider, String status) {
        this.nome = nome;
        this.dataCriacao = data;
        this.nomeLider = nomeLider;
        this.status = status;
    }

    public Ministerio() {
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getNomeLider() {
        return nomeLider;
    }

    public void setNomeLider(String nomeLider) {
        this.nomeLider = nomeLider;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

