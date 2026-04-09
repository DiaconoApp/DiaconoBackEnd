package com.diacono.diacono.applications.dtos.membro;

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