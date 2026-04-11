package com.diacono.diacono.infrastructure.auth;

import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.domain.auth.GoogleIdTokenClaims;
import com.diacono.diacono.domain.auth.GoogleIdTokenVerifier;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@Component
public class GoogleIdTokenVerifierImpl implements GoogleIdTokenVerifier {

    private final JwtDecoder googleJwtDecoder;

    public GoogleIdTokenVerifierImpl() {
        this.googleJwtDecoder = JwtDecoders.fromOidcIssuerLocation("https://accounts.google.com");
    }

    @Override
    public GoogleIdTokenClaims verify(String idToken) {
        try {
            Jwt googleJwt = googleJwtDecoder.decode(idToken);
            return new GoogleIdTokenClaims(
                    googleJwt.getAudience(),
                    googleJwt.getClaimAsString("email"),
                    googleJwt.getClaim("email_verified")
            );
        } catch (JwtException exception) {
            throw new BadCredentialsException("Token do Google invalido");
        }
    }
}
