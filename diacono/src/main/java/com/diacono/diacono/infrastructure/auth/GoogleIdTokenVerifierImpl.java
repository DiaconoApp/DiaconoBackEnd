package com.diacono.diacono.infrastructure.auth;

import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.applications.dtos.googleauth.GoogleIdTokenDTO;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class GoogleIdTokenVerifierImpl implements GoogleIdTokenVerifier {
    private static final String GOOGLE_ISSUER = "https://accounts.google.com";
    private static final String GOOGLE_JWK_SET_URI = "https://www.googleapis.com/oauth2/v3/certs";

    private final JwtDecoder googleJwtDecoder;

    public GoogleIdTokenVerifierImpl() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(GOOGLE_JWK_SET_URI).build();
        jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(GOOGLE_ISSUER));
        this.googleJwtDecoder = jwtDecoder;
    }

    @Override
    public GoogleIdTokenDTO verify(String idToken) {
        try {
            Jwt googleJwt = googleJwtDecoder.decode(idToken);
            return new GoogleIdTokenDTO(
                    googleJwt.getAudience(),
                    googleJwt.getClaimAsString("email"),
                    googleJwt.getClaim("email_verified")
            );
        } catch (JwtException exception) {
            throw new BadCredentialsException("Token do Google invalido");
        }
    }
}
