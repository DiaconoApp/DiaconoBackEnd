package com.diacono.diacono.domain.entity;

import com.diacono.diacono.global.util.IdEntityUtils;
import com.diacono.diacono.global.util.SensitiveSearchIndexUtils;
import com.diacono.diacono.global.util.SensitiveStringAttributeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "token_google")
public class GoogleRefreshTokenMembro extends IdEntityUtils {

    @Column(name = "membro_id", nullable = false)
    @NotNull(message = "O membroId não pode ser nulo")
    private UUID membroId;

    @Column(name = "igreja_id", nullable = false)
    @NotNull(message = "IgrejaId não pode ser nulo")
    private UUID igrejaId;

    @Column(nullable = false)
    @NotBlank(message = "Email não pode estar em branco")
    @Convert(converter = SensitiveStringAttributeConverter.class)
    private String email;

    @Column(name = "email_hash", length = 32)
    private String emailHash;

    @Column(name = "refresh_token", nullable = false, length = 2048)
    private String refreshToken;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void atualizarIndicesCamposSensiveis() {
        this.emailHash = SensitiveSearchIndexUtils.exactHash(this.email);
    }

    public boolean possuiIndicesSensiveis() {
        return emailHash != null;
    }
}
