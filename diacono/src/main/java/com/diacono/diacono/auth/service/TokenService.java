package com.diacono.diacono.auth.service;

import com.diacono.diacono.membro.model.entity.Membro;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

// TODO: Alterar Issuer para URL da API
@Service
public class TokenService {

    // OWASP A05/A07: limites para evitar tokens sem expiracao ou com TTL excessivo por misconfig.
    private static final long DEFAULT_EXPIRATION_SECONDS = 3600L;
    private static final long MIN_EXPIRATION_SECONDS = 300L;
    private static final long MAX_EXPIRATION_SECONDS = 86400L;
    private static final String GENERIC_TOKEN_ERROR = "Falha na autenticacao";

    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.expiration-seconds:3600}")
    private long expiresIn;

    @Value("${app.jwt.issuer:diacono-api}")
    private String issuer;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public long getExpiresIn() {
        return sanitizeExpirationSeconds(this.expiresIn);
    }

    public String generateToken(Membro membro) {
        var now = Instant.now();
        long effectiveExpiresIn = getExpiresIn();

        // OWASP A01/A02: token com privilegio minimo; evita dados pessoais desnecessarios no JWT.
        var claims = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .issuer(sanitizeIssuer())
                .subject(membro.getIdExterno().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(effectiveExpiresIn))
                .claim("scope", membro.getCargoMembro().name())
                .claim("nome", membro.getNome())
                .claim("fk_igreja", membro.getIgreja().getIdExterno().toString())
                .claim("igreja", membro.getIgreja().getNome())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private long sanitizeExpirationSeconds(long configuredValue) {
        if (configuredValue < MIN_EXPIRATION_SECONDS || configuredValue > MAX_EXPIRATION_SECONDS) {
            return DEFAULT_EXPIRATION_SECONDS;
        }
        return configuredValue;
    }

    private String sanitizeIssuer() {
        if (issuer == null || issuer.isBlank()) {
            return "diacono-api";
        }
        return issuer.trim();
    }
}