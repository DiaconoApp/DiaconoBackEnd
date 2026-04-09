package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.login.GoogleAuthRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.use_cases.GoogleAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/google")
public class GoogleAuthController {

	private final GoogleAuthService googleAuthService;

	public GoogleAuthController(GoogleAuthService googleAuthService) {
		this.googleAuthService = googleAuthService;
	}

	@PostMapping
	public ResponseEntity<LoginResponseDTO> autenticarComGoogle(@RequestBody GoogleAuthRequestDTO googleAuthRequestDTO) {
		return ResponseEntity.ok(googleAuthService.autenticar(googleAuthRequestDTO));
	}
}
