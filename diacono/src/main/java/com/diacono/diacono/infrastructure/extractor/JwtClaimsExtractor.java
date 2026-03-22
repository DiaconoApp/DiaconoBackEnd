package com.diacono.diacono.infrastructure.extractor;

import com.diacono.diacono.presentation.exception.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtClaimsExtractor {

    public UUID getSubject() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return UUID.fromString(jwt.getSubject());
    }

    public UUID getIgrejaId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        String claimValue = jwt.getClaim("fk_igreja");

        if (claimValue == null) {
            throw new BadCredentialsException("Usuário não validado.");
        }

        return UUID.fromString(claimValue);
    }





}
