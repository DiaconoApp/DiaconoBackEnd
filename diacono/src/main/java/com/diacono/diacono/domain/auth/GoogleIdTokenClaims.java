package com.diacono.diacono.domain.auth;

import java.util.List;

public record GoogleIdTokenClaims(
        List<String> audience,
        String email,
        Boolean emailVerified
) {
}

