package com.diacono.diacono.infrastructure.auth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleAuthorizationCodeRequestDTO;
import com.diacono.diacono.applications.dtos.googleauth.GoogleTokenResponseDTO;

public interface GoogleAuthorizationCodeExchanger {

    GoogleTokenResponseDTO exchange(GoogleAuthorizationCodeRequestDTO request);
}
