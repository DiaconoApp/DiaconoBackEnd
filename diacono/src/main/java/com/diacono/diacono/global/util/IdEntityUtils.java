package com.diacono.diacono.global.util;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@MappedSuperclass
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class IdEntityUtils {

    // 1. CHAVE INTERNA
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long idInterno;

    // 2. CHAVE EXTERNA
    @Column(name = "id_externo", unique = true, nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID idExterno;

    @PrePersist
    protected void onCreate() {
        if (this.idExterno == null) {
            this.idExterno = UUID.randomUUID();
        }
    }
}
