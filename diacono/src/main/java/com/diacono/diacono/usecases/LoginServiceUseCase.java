package com.diacono.diacono.usecases;

import com.diacono.diacono.applications.dtos.login.LoginRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.usecases.membro.BuscarPorEmaiUseCase;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceUseCase {

    private final BuscarPorEmaiUseCase buscarPorEmaiUseCase;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final GenerateTokenUseCase generateTokenUseCase;

    public LoginServiceUseCase(BuscarPorEmaiUseCase buscarPorEmaiUseCase, BCryptPasswordEncoder bCryptPasswordEncoder, GenerateTokenUseCase generateTokenUseCase) {
        this.buscarPorEmaiUseCase = buscarPorEmaiUseCase;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.generateTokenUseCase = generateTokenUseCase;
    }

    public LoginResponseDTO execute(LoginRequestDTO loginRequestDTO){

        Membro membro = buscarPorEmaiUseCase.execute(loginRequestDTO.email());

        if(membro == null || !bCryptPasswordEncoder.matches(loginRequestDTO.senha(), membro.getSenha())){
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }

        String jwtValue = generateTokenUseCase.execute(membro);
        long expiresIn = generateTokenUseCase.getExpiresIn();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }

}