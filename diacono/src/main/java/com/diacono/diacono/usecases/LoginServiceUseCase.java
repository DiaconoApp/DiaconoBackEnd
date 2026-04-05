package com.diacono.diacono.usecases;

import com.diacono.diacono.applications.dtos.login.LoginRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.domain.entity.Membro;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceUseCase {

    private final MembroService membroService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;

    public LoginServiceUseCase(MembroService membroService, BCryptPasswordEncoder bCryptPasswordEncoder, TokenService tokenService) {
        this.membroService = membroService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenService = tokenService;
    }

    public LoginResponseDTO execute(LoginRequestDTO loginRequestDTO){

        Membro membro = membroService.buscarPorEmail(loginRequestDTO.email());

        if(membro == null || !bCryptPasswordEncoder.matches(loginRequestDTO.senha(), membro.getSenha())){
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }

        String jwtValue = tokenService.generateToken(membro);
        long expiresIn = tokenService.getExpiresIn();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }

}