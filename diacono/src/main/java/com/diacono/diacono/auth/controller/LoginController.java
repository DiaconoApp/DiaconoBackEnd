package com.diacono.diacono.auth.controller;

import com.diacono.diacono.auth.model.dto.request.LoginRequestDTO;
import com.diacono.diacono.auth.model.dto.response.LoginResponseDTO;
import com.diacono.diacono.auth.service.LoginService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsável pelo gerenciamento de autenticação
 * OWASP A07: Implementa proteções contra falhas de autenticação
 */
@RestController
@RequestMapping("/api/v1/auth/login")
public class LoginController {

    // OWASP A05: Logging seguro - não expõe dados sensíveis em logs
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Endpoint de autenticação com validações de segurança
     *
     * OWASP A01: Validação de entrada com @Valid
     * OWASP A02: Delegação de verificação criptográfica ao LoginService
     * OWASP A05: Headers de segurança configurados
     * OWASP A07: Proteção contra ataques de autenticação
     *
     * @param loginRequestDTO Credenciais do usuário validadas
     * @return ResponseEntity com token JWT e tempo de expiração
     */
    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){

        // OWASP A05: Log sem expor dados sensíveis (não loga senha)
        logger.info("Tentativa de login para email: {}",
            loginRequestDTO.email() != null ? maskEmail(loginRequestDTO.email()) : "null");

        // OWASP A02: Validação de senha e geração de token delegadas ao service
        // que usa BCryptPasswordEncoder (algoritmo seguro)
        LoginResponseDTO response = loginService.validarLogin(loginRequestDTO);

        // OWASP A05: Headers de segurança adicionados à resposta
        HttpHeaders headers = new HttpHeaders();
        // Previne cache de credenciais
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        headers.add("Pragma", "no-cache");
        // Proteção adicional contra XSS
        headers.add("X-Content-Type-Options", "nosniff");

        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
    }

    /**
     * OWASP A05: Método auxiliar para mascarar email em logs
     * Evita exposição completa de informações sensíveis
     */
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }
        String[] parts = email.split("@");
        String localPart = parts[0];
        return localPart.substring(0, 2) + "***@" + parts[1];
    }

}
