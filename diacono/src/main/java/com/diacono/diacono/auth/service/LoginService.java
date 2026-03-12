package com.diacono.diacono.auth.service;

import com.diacono.diacono.auth.model.dto.request.LoginRequestDTO;
import com.diacono.diacono.auth.model.dto.response.LoginResponseDTO;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.service.MembroService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LoginService {
    // OWASP A05: resposta de erro unica para evitar vazamento de detalhes.
    private static final String GENERIC_AUTH_ERROR = "Usuario ou senha invalidos";

    // OWASP A02/A07: hash valido usado quando usuario nao existe (mitiga timing attack/enumeração).
    private static final String DUMMY_BCRYPT_HASH = "$2a$10$7EqJtq98hPqEX7fNZaFWoOHi9M8n5YQbY4Z1cdq9VHtV6nRvWmvFy";

    private final MembroService membroService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;

    public LoginService(MembroService membroService, BCryptPasswordEncoder bCryptPasswordEncoder, TokenService tokenService) {
        this.membroService = membroService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenService = tokenService;
    }

    public LoginResponseDTO validarLogin(LoginRequestDTO loginRequestDTO){
        // OWASP A01: validacao defensiva no backend para evitar entrada invalida.
        if (loginRequestDTO == null || isBlank(loginRequestDTO.email()) || isBlank(loginRequestDTO.senha())) {
            throw new BadCredentialsException(GENERIC_AUTH_ERROR);
        }

        String emailNormalizado = normalizeEmail(loginRequestDTO.email());
        String senha = loginRequestDTO.senha();

        // OWASP A01: limites para reduzir abuso de payload e entradas inconsistentes.
        if (emailNormalizado.length() > 254 || senha.length() > 128) {
            throw new BadCredentialsException(GENERIC_AUTH_ERROR);
        }

        Membro membro = membroService.buscarPorEmail(emailNormalizado);

        // OWASP A07: compara bcrypt mesmo sem usuario para reduzir diferenca de tempo entre cenarios.
        String senhaHash = (membro != null && membro.getSenha() != null) ? membro.getSenha() : DUMMY_BCRYPT_HASH;
        boolean senhaValida = bCryptPasswordEncoder.matches(senha, senhaHash);

        if (membro == null || !senhaValida) {
            throw new BadCredentialsException(GENERIC_AUTH_ERROR);
        }

        String jwtValue = tokenService.generateToken(membro);
        long expiresIn = tokenService.getExpiresIn();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }

    private boolean isBlank(String valor) {
        return valor == null || valor.isBlank();
    }

    private String normalizeEmail(String email) {
        // OWASP A01: normalizacao reduz bypass por variacao de caixa/espacos.
        return email.trim().toLowerCase(Locale.ROOT);
    }
}