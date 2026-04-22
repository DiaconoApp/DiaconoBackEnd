package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.googleauth.GoogleAuthRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.usecases.googleauth.LoginGoogleUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth/google")
public class GoogleAuthController {

    private static final Logger logger = LoggerFactory.getLogger(GoogleAuthController.class);
    private final LoginGoogleUseCase loginGoogleUseCase;

    public GoogleAuthController(LoginGoogleUseCase loginGoogleUseCase) {
        this.loginGoogleUseCase = loginGoogleUseCase;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> autenticarComGoogle(@Valid @RequestBody GoogleAuthRequestDTO googleAuthRequestDTO) {
        try {
            LoginResponseDTO response = loginGoogleUseCase.execute(googleAuthRequestDTO);
            logger.info("Autenticação Google realizado com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.warn("Autenticação Google falhou: {}. Verifique o audience e verificação de email", e.getMessage());
            throw e;
        }
    }
}
