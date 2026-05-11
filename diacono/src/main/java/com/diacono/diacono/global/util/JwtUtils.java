package com.diacono.diacono.global.util;

import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private Jwt extractJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            logger.warn("Tentativa de acesso sem autenticação no SecurityContext.");
            throw new BadCredentialsException("Usuário não autenticado.");
        }

        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            logger.warn("Principal do SecurityContext não é um JWT válido. tipo=[{}]",
                    authentication.getPrincipal().getClass().getSimpleName());
            throw new BadCredentialsException("Usuário não autenticado.");
        }

        return jwt;
    }

    public UUID getSubject() {
        Jwt jwt = extractJwt();

        String subject = jwt.getSubject();

        if (subject == null || subject.isBlank()) {
            logger.warn("JWT sem claim 'sub'.");
            throw new BadCredentialsException("Usuário não validado.");
        }

        try {
            return UUID.fromString(subject);
        } catch (IllegalArgumentException e) {
            logger.warn("Claim 'sub' do JWT com formato inválido.");
            throw new BadCredentialsException("Usuário não validado.");
        }
    }

    public UUID getIgrejaId() {
        Jwt jwt = extractJwt();

        String claimValue = jwt.getClaim("fk_igreja");

        if (claimValue == null || claimValue.isBlank()) {
            logger.warn("JWT sem claim 'fk_igreja' — acesso negado.");
            throw new BadCredentialsException("Usuário não validado.");
        }

        try {
            return UUID.fromString(claimValue);
        } catch (IllegalArgumentException e) {
            logger.warn("Claim 'fk_igreja' do JWT com formato inválido.");
            throw new BadCredentialsException("Usuário não validado.");
        }
    }

    public EnumCargoMembro getCargo() {
        Jwt jwt = extractJwt();

        String scope = jwt.getClaim("scope");

        if (scope == null || scope.isBlank()) {
            logger.warn("JWT sem claim 'scope' — acesso negado.");
            throw new BadCredentialsException("Usuário não validado.");
        }

        try {
            return EnumCargoMembro.valueOf(scope.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Claim 'scope' do JWT com valor não mapeável para EnumCargoMembro. valor=[{}]", scope);
            throw new BadCredentialsException("Usuário não validado.");
        }
    }
}