package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.googleauth.GoogleAuthorizationCodeRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.usecases.googleauth.LoginGoogleUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/google")
public class GoogleAuthController {

    private final LoginGoogleUseCase loginGoogleUseCase;

    public GoogleAuthController(LoginGoogleUseCase loginGoogleUseCase) {
        this.loginGoogleUseCase = loginGoogleUseCase;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> autenticarComGoogle(@Valid @RequestBody GoogleAuthorizationCodeRequestDTO googleAuthRequestDTO) {
        return ResponseEntity.ok(loginGoogleUseCase.execute(googleAuthRequestDTO));
    }
}
