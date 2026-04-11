package com.diacono.diacono.domain.auth;

public interface GoogleIdTokenVerifier {
    GoogleIdTokenClaims verify(String idToken);
}


