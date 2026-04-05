package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.login.LoginRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.usecases.LoginServiceUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/login")
public class LoginController {

    private final LoginServiceUseCase loginServiceUseCase;

    public LoginController(LoginServiceUseCase loginServiceUseCase) {
        this.loginServiceUseCase = loginServiceUseCase;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){

        return ResponseEntity.ok(loginServiceUseCase.execute(loginRequestDTO));
    }
}
