package com.diacono.diacono.membro.model.dto.response;

public class MembroDashEvolucaoDTO {

    private Integer ano;
    private Long quantidade;

    public MembroDashEvolucaoDTO(Object ano, Long quantidade) {
        this.ano = ano != null ? ((Number) ano).intValue() : null;
        this.quantidade = quantidade;
    }

    public Integer getAno() {
        return ano;
    }

    public Long getQuantidade() {
        return quantidade;
    }
}