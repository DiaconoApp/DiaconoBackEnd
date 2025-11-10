package com.diacono.diacono.global.util;

import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtUtils {

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
