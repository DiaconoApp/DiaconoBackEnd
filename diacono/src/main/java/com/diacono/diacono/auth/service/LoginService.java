package com.diacono.diacono.auth.service;

import com.diacono.diacono.auth.model.dto.request.LoginRequestDTO;
import com.diacono.diacono.auth.model.dto.response.LoginResponseDTO;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.service.MembroService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final MembroService membroService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;

    public LoginService(MembroService membroService, BCryptPasswordEncoder bCryptPasswordEncoder, TokenService tokenService) {
        this.membroService = membroService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenService = tokenService;
    }

    public LoginResponseDTO validarLogin(LoginRequestDTO loginRequestDTO){

        Membro membro = membroService.buscarPorEmail(loginRequestDTO.email());

        if(membro == null || !bCryptPasswordEncoder.matches(loginRequestDTO.senha(), membro.getSenha())){
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }

        String jwtValue = tokenService.generateToken(membro);
        long expiresIn = tokenService.getExpiresIn();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }

}