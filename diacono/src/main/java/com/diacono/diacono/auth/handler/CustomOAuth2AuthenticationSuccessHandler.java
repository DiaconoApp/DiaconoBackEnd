package com.diacono.diacono.auth.handler;

import com.diacono.diacono.auth.service.TokenService;
import com.diacono.diacono.auth.model.dto.response.LoginResponseDTO;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.service.MembroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Locale;

/**
 * Handler customizado para sucesso de autenticacao OAuth2/OIDC
 * OWASP A07: Implementa controles adicionais apos autenticacao bem-sucedida
 */
@Component
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // OWASP A05: Logging seguro em vez de System.out
    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2AuthenticationSuccessHandler.class);

    private static final String GENERIC_ERROR_MESSAGE = "Falha na autenticacao";

    private final TokenService tokenService;
    private final MembroService membroService;
    private final ObjectMapper objectMapper;

    public CustomOAuth2AuthenticationSuccessHandler(
            TokenService tokenService,
            MembroService membroService,
            ObjectMapper objectMapper
    ) {
        this.tokenService = tokenService;
        this.membroService = membroService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        try {
            // OWASP A07: Valida principal de autenticacao antes de processar
            if (!(authentication.getPrincipal() instanceof OAuth2User)) {
                logger.error("Principal de autenticacao OAuth2 invalido");
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String email = oauth2User.getAttribute("email");

            // OWASP A07: Valida presenca de email no claim
            if (email == null || email.isBlank()) {
                logger.warn("Tentativa de autenticacao OAuth2 sem claim 'email'");
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // OWASP A02/A05: Normaliza email para consistencia
            String emailNormalizado = normalizeEmail(email);

            Membro membro = membroService.buscarPorEmail(emailNormalizado);

            // OWASP A07: Valida existencia do membro apos autenticacao OAuth2
            if (membro == null) {
                // OWASP A05: Log interno detalhado sem expor email completo
                logger.error("Membro nao encontrado apos autenticacao OAuth2 para dominio: {}",
                    extractDomain(emailNormalizado));
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // OWASP A01/A07: Somente membros ativos podem receber token JWT
            if (!EnumStatusMembro.ATIVO.equals(membro.getStatus())) {
                logger.warn("Tentativa de autenticacao OAuth2 com conta inativa para email: {}",
                    maskEmail(emailNormalizado));
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // OWASP A02/A07: Gera token JWT com validacoes ja implementadas no TokenService
            String jwtToken = tokenService.generateToken(membro);
            long expiresIn = tokenService.getExpiresIn();

            LoginResponseDTO responseDto = new LoginResponseDTO(jwtToken, expiresIn);

            // OWASP A05: Headers de seguranca na resposta
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            // Previne cache de credenciais
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            response.setHeader("Pragma", "no-cache");
            // Protecao adicional contra XSS
            response.setHeader("X-Content-Type-Options", "nosniff");

            response.getWriter().write(objectMapper.writeValueAsString(responseDto));
            response.getWriter().flush();

            logger.info("Autenticacao OAuth2 bem-sucedida e token JWT emitido");

        } catch (Exception e) {
            // OWASP A05: Trata excecoes sem expor stack trace ao cliente
            logger.error("Erro inesperado no handler de sucesso OAuth2: {}", e.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // OWASP A05: Metodo auxiliar para enviar respostas de erro genericas
    private void sendErrorResponse(HttpServletResponse response, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String errorJson = String.format("{\"error\": \"%s\"}", GENERIC_ERROR_MESSAGE);
        response.getWriter().write(errorJson);
        response.getWriter().flush();
    }

    // OWASP A02/A05: Normaliza email para consistencia
    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    // OWASP A05: Mascara email para logs seguros
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }
        String[] parts = email.split("@");
        String localPart = parts[0];
        return localPart.substring(0, 2) + "***@" + parts[1];
    }

    // OWASP A05: Extrai dominio para auditoria sem expor identidade
    private String extractDomain(String email) {
        if (email == null || !email.contains("@")) {
            return "unknown";
        }
        return email.substring(email.indexOf("@") + 1);
    }
}