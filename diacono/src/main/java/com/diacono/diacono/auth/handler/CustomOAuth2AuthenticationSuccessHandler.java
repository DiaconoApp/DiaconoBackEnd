package com.diacono.diacono.auth.handler;

import com.diacono.diacono.auth.service.TokenService;
import com.diacono.diacono.auth.model.dto.response.LoginResponseDTO;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.service.MembroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

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

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");

        Membro membro = membroService.buscarPorEmail(email);

        if (membro == null) {
            System.out.println("Erro: Membro não encontrado para o email: " + email);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Usuário não encontrado após login OAuth2.\"}");
            return;
        }

        String jwtToken = tokenService.generateToken(membro);
        long expiresIn = tokenService.getExpiresIn();

        LoginResponseDTO responseDto = new LoginResponseDTO(jwtToken, expiresIn);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));

        response.getWriter().flush();
    }

}