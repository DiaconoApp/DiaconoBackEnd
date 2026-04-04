package com.diacono.diacono.usecases;

import com.diacono.diacono.domain.entity.Membro;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Getter
public class TokenService {

    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.expiration-seconds:3600}")
    private long expiresIn;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

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
}