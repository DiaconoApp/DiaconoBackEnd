package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.googleauth.GoogleAuthRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.usecases.googleauth.LoginGoogleAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/google")
public class GoogleAuthController {

    private final LoginGoogleAuthService LoginGoogleAuthService;

    public GoogleAuthController(LoginGoogleAuthService googleAuthService) {
        this.LoginGoogleAuthService = googleAuthService;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> autenticarComGoogle(@RequestBody GoogleAuthRequestDTO googleAuthRequestDTO) {
        return ResponseEntity.ok(LoginGoogleAuthService.execute(googleAuthRequestDTO));
    }
}
