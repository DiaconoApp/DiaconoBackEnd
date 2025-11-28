package com.diacono.diacono.membro.model.dto.response;

public class MembroDashEvolucaoDTO{

    private Integer ano;
    private Long quantidade;

    public MembroDashEvolucaoDTO(Integer ano, Long quantidade) {
        this.ano = ano;
        this.quantidade = quantidade;
    }

    public Integer getAno() {
        return ano;
    }

    public Long getQuantidade() {
        return quantidade;
    }

}
