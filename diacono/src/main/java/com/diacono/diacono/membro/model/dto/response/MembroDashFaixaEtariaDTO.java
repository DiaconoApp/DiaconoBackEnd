package com.diacono.diacono.membro.model.dto.response;

public record MembroDashFaixaEtariaDTO(
        long criancas,
        long adolescentes,
        long jovens,
        long adultos,
        long idosos
){
    public MembroDashFaixaEtariaDTO(Number criancas, Number adolescentes, Number jovens, Number adultos, Number idosos) {
        this(
                safeLong(criancas),
                safeLong(adolescentes),
                safeLong(jovens),
                safeLong(adultos),
                safeLong(idosos)
        );
    }

    private static long safeLong(Number number) {
        return number != null ? number.longValue() : 0L;
    }
}