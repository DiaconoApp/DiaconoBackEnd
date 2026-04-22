package com.diacono.diacono.usecases;

import com.diacono.diacono.applications.dtos.login.LoginRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.usecases.membro.BuscarPorEmaiUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceUseCase {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceUseCase.class);
    private static final String INVALID_CREDENTIALS_MESSAGE = "Usuário ou senha inválidos";
    private static final String DUMMY_BCRYPT_HASH = "$2a$10$7EqJtq98hPqEX7fNZaFWoOeRrjS4V0LlwM651c01qmPvvrLpzjAU6";

    private final BuscarPorEmaiUseCase buscarPorEmaiUseCase;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final GenerateTokenUseCase generateTokenUseCase;

    public LoginServiceUseCase(BuscarPorEmaiUseCase buscarPorEmaiUseCase, BCryptPasswordEncoder bCryptPasswordEncoder, GenerateTokenUseCase generateTokenUseCase) {
        this.buscarPorEmaiUseCase = buscarPorEmaiUseCase;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.generateTokenUseCase = generateTokenUseCase;
    }

    public LoginResponseDTO execute(LoginRequestDTO loginRequestDTO){

        Membro membro;
        try {
            membro = buscarPorEmaiUseCase.execute(loginRequestDTO.email());
        } catch (ObjectNotFoundException ex) {
            // Mantem tempo de resposta semelhante ao caso de senha incorreta para reduzir enumeração de usuário.
            matchesSafely(loginRequestDTO.senha(), DUMMY_BCRYPT_HASH);
            logger.warn("Falha de autenticacao: credenciais inválidas.");
            throw new BadCredentialsException(INVALID_CREDENTIALS_MESSAGE);
        }

        if(membro == null || !matchesSafely(loginRequestDTO.senha(), membro.getSenha())){
            logger.warn("Falha de autenticacao: credenciais inválidas.");
            throw new BadCredentialsException(INVALID_CREDENTIALS_MESSAGE);
        }

        String jwtValue = generateTokenUseCase.execute(membro);
        long expiresIn = generateTokenUseCase.getExpiresIn();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }

    private boolean matchesSafely(String rawPassword, String encodedPassword) {
        try {
            return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
        } catch (IllegalArgumentException ex) {
            logger.warn("Falha ao validar hash de senha.");
            return false;
        }
    }

}