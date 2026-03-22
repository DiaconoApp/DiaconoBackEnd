package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.service.TokenService;
import com.diacono.diacono.domain.entities.Membro;
import com.diacono.diacono.presentation.exception.BadCredentialsException;
import com.diacono.diacono.infrastructure.persistence.MembroRepository;
import com.diacono.diacono.presentation.dto.request.LoginRequestDTO;
import com.diacono.diacono.presentation.dto.response.LoginResponseDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {

    private final MembroRepository membroRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;

    public LoginUseCase(MembroRepository membroRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenService tokenService) {
        this.membroRepository = membroRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenService = tokenService;
    }

    public LoginResponseDTO execute(LoginRequestDTO loginRequestDTO){

        Membro membro = membroRepository.findByEmail(loginRequestDTO.email());

        if(membro == null || !bCryptPasswordEncoder.matches(loginRequestDTO.senha(), membro.getSenha())){
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }

        String jwtValue = tokenService.generateToken(membro);
        long expiresIn = tokenService.expiresIn();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }

}
