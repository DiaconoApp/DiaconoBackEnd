package com.diacono.diacono.usecases.membro.validation;

import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ValidarCriacaoMembro {

    private final BCryptPasswordEncoder passwordEncoder;

    public ValidarCriacaoMembro(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void validaCriacao(Membro membro) {
        if (membro == null) {
            throw new ObjectSaveErrorException("Não foi possível cadastrar o usuário");
        }
    }

    public String hashSenha(String senha) {
        return passwordEncoder.encode(senha);
    }
}