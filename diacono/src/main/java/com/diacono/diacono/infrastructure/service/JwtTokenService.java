package com.diacono.diacono.infrastructure.service;

import com.diacono.diacono.application.service.TokenService;
import com.diacono.diacono.domain.entities.Membro;

import java.time.Instant;

@Service
public class JwtTokenService implements TokenService {

    @Value("${app.jwt.expiration-seconds:3600}")
    private long expiresIn;

    @Override
    public String generateToken(Membro membro) {
        var now = Instant.now();

        String scopeString = membro.getCargoMembro().name();

        var claims = JwtClaimsSet.builder()
                .issuer("diacono-api")
                .subject((membro.getIdExterno()).toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(this.expiresIn)) // Usa a propriedade injetada
                .claim("scope", scopeString)
                .claim("nome", membro.getNome())
                .claim("idade", membro.getDataNascimento().toString())
                .claim("fk_igreja", membro.getIgreja().getIdExterno())
                .claim("igreja", membro.getIgreja().getNome())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public long expiresIn() {
        return expiresIn;
    }
}
