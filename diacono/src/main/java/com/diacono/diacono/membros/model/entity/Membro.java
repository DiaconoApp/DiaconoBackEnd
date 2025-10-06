package com.diacono.diacono.membros.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Membro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String email;
    private String celular;
    private String senha;
    private String ministerio;
    private Boolean ativo = true;
    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private EnderecoMembro enderecoMembro;
    @ManyToOne
    @JoinColumn(name = "discipulador_id")
    private Membro discipulador;

    public Membro(Integer id, String nome, String cpf, LocalDate dataNascimento, String email, String celular, String senha, String ministerio, Boolean ativo, EnderecoMembro enderecoMembro) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.celular = celular;
        this.senha = senha;
        this.ministerio = ministerio;
        this.ativo = ativo;
        this.enderecoMembro = enderecoMembro;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public String getCelular() {
        return celular;
    }

    public String getSenha() {
        return senha;
    }

    public String getMinisterio() {
        return ministerio;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public Membro getDiscipulador() {
        return discipulador;
    }

    public void setDiscipulador(Membro discipulador) {
        this.discipulador = discipulador;
    }

    public EnderecoMembro getEnderecoMembro() {
        return enderecoMembro;
    }
}
