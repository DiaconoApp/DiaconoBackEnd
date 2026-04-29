package com.diacono.diacono.domain.entity;

import com.diacono.diacono.global.util.IdEntityUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class GoogleRefreshTokenMembro extends IdEntityUtils {

    @Column(name = "membro_id", nullable = false)
    private UUID membroId;

    @Column(nullable = false)
    private String email;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;
}

