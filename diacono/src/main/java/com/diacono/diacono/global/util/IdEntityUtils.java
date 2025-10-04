package com.diacono.diacono.global.util;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@MappedSuperclass
@Getter
@NoArgsConstructor
public abstract class IdEntityUtils {

    // 1. CHAVE INTERNA
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInterno;

    // 2. CHAVE EXTERNA
    @Column(name = "id_externo", unique = true, nullable = false, updatable = false)
    private UUID idExterno;

    @PrePersist
    protected void onCreate() {
        if (this.idExterno == null) {
            this.idExterno = UUID.randomUUID();
        }
    }

}
