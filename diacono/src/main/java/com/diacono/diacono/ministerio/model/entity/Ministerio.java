package com.diacono.diacono.ministerio.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Ministerio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

