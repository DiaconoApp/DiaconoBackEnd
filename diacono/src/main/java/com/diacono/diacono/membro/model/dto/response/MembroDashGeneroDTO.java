package com.diacono.diacono.membro.model.dto.response;

public record MembroDashGeneroDTO(
        long masculino,
        long feminino
) {
    public MembroDashGeneroDTO(Number masculino, Number feminino) {
        this(
                safeLong(masculino),
                safeLong(feminino)
        );
    }

    private static long safeLong(Number number) {
        return number != null ? number.longValue() : 0L;
    }
}