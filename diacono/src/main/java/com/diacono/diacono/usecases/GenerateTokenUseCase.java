package com.diacono.diacono.usecases;

import com.diacono.diacono.domain.entity.Membro;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
@Getter
public class GenerateTokenUseCase {

    private static final Logger logger = LoggerFactory.getLogger(GenerateTokenUseCase.class);

    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.expiration-seconds:3600}")
    private long expiresIn;

    public GenerateTokenUseCase(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String execute(Membro membro) {
        if (membro == null
                || membro.getIdExterno() == null
                || membro.getCargoMembro() == null
                || membro.getIgreja() == null
                || membro.getIgreja().getIdExterno() == null) {
            String memberId = membro != null && membro.getIdExterno() != null ? membro.getIdExterno().toString() : "desconhecido";
            logger.warn("Falha ao gerar JWT: membro sem dados minimos de autenticacao/autorizacao. memberId={}", memberId);
            throw new IllegalArgumentException("Membro inválido para geração de token.");
        }

        var now = Instant.now();

        String scopeString = membro.getCargoMembro().name();

        var claims = JwtClaimsSet.builder()
                .issuer("diacono-api")
                .subject((membro.getIdExterno()).toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(this.expiresIn))
                .claim("scope", scopeString)
                .claim("nome", Objects.toString(membro.getNome()))
                .claim("idade", Objects.toString(membro.getDataNascimento()))
                .claim("fk_igreja", membro.getIgreja().getIdExterno())
                .claim("igreja", Objects.toString(membro.getIgreja().getNome()))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}