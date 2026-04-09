package com.diacono.diacono.auth.handler;

import com.diacono.diacono.usecases.GenerateTokenUseCase;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.usecases.membro.BuscarPorEmaiUseCase;
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

    private final GenerateTokenUseCase generateTokenUseCase;
    private final BuscarPorEmaiUseCase buscarPorEmaiUseCase;
    private final ObjectMapper objectMapper;


    public CustomOAuth2AuthenticationSuccessHandler(
            GenerateTokenUseCase generateTokenUseCase,
            BuscarPorEmaiUseCase buscarPorEmaiUseCase,
            ObjectMapper objectMapper
    ) {
        this.generateTokenUseCase = generateTokenUseCase;
        this.buscarPorEmaiUseCase = buscarPorEmaiUseCase;
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

        Membro membro = buscarPorEmaiUseCase.execute(email);

        if (membro == null) {
            System.out.println("Erro: Membro não encontrado para o email: " + email);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Usuário não encontrado após login OAuth2.\"}");
            return;
        }

        String jwtToken = generateTokenUseCase.execute(membro);
        long expiresIn = generateTokenUseCase.getExpiresIn();

        LoginResponseDTO responseDto = new LoginResponseDTO(jwtToken, expiresIn);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));

        response.getWriter().flush();
    }

}