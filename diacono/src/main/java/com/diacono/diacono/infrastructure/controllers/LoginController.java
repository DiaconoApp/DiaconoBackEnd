package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.login.LoginRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.usecases.LoginServiceUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth/login")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final LoginServiceUseCase loginServiceUseCase;

    public LoginController(LoginServiceUseCase loginServiceUseCase) {
        this.loginServiceUseCase = loginServiceUseCase;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        try {
            return ResponseEntity.ok(loginServiceUseCase.execute(loginRequestDTO));
        } catch (RuntimeException ex) {
            logger.warn("Falha na autenticação local: {}", ex.getClass().getSimpleName());
            throw ex;
        }
    }
}
