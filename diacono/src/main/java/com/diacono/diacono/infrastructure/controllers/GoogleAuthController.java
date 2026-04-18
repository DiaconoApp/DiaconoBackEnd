package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.googleauth.GoogleAuthRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.usecases.googleauth.LoginGoogleUseCase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth/google")
public class GoogleAuthController {

    private final LoginGoogleUseCase loginGoogleUseCase;

    public GoogleAuthController(LoginGoogleUseCase loginGoogleUseCase) {
        this.loginGoogleUseCase = loginGoogleUseCase;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> autenticarComGoogle(@RequestBody GoogleAuthRequestDTO googleAuthRequestDTO) {
        LoginResponseDTO loginResponse = loginGoogleUseCase.execute(googleAuthRequestDTO);

        ResponseCookie cookie = ResponseCookie.from("auth_token", loginResponse.acessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofSeconds(loginResponse.expiresIn()))
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse);
    }
}
