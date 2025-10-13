package com.diacono.diacono.membro.model.entity;

import com.diacono.diacono.global.util.IdEntityUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@SuperBuilder(toBuilder = true)

public class Membro extends IdEntityUtils {

    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String cpf;
    private String cep;
    private Integer numeroCasa;
    private String senhaTemporaria;
    private String senha;

    public Membro(String nome, String email, LocalDate dataNascimento, String cpf, String cep, Integer numeroCasa, String senhaTemporaria, String senha) {
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.cep = cep;
        this.numeroCasa = numeroCasa;
        this.senhaTemporaria = senhaTemporaria;
        this.senha = senha;
    }

    public Membro() {
    }



    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCep() {
        return cep;
    }

    public Integer getNumeroCasa() {
        return numeroCasa;
    }

    public String getSenhaTemporaria() {
        return senhaTemporaria;
    }

    public String getSenha() {
        return senha;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setNumeroCasa(Integer numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
