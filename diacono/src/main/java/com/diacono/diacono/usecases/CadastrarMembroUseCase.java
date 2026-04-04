package com.diacono.diacono.usecases;

import com.diacono.diacono.applications.dtos.CadastroExternoDTO;
import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.entity.Membro;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CadastrarMembroUseCase {

    private final MembroService membroService;

    public CadastrarMembroUseCase(MembroService membroService) {
        this.membroService = membroService;
    }

    public RestResponseMessageDTO execute(CadastroExternoDTO cadastroDTO) {

        Membro membro = membroService.criarMembroExterno(cadastroDTO);

        return new RestResponseMessageDTO(HttpStatus.CREATED, "Usuário cadastrado com sucesso");
    }
}