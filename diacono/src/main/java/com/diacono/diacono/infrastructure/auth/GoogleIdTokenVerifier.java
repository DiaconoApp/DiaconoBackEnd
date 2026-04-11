package com.diacono.diacono.infrastructure.auth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleIdTokenDTO;

public interface GoogleIdTokenVerifier {
    GoogleIdTokenDTO verify(String idToken);
}


