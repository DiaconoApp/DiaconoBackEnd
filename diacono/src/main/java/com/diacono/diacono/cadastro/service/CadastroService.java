package com.diacono.diacono.cadastro.service;

import com.diacono.diacono.Igreja.model.dto.response.IgrejaSemiCompletoDTO;
import com.diacono.diacono.Igreja.service.IgrejaService;
import com.diacono.diacono.cadastro.model.dto.CadastroExternoDTO;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.service.MembroService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
public class CadastroService {

    private final IgrejaService igrejaService;
    private final MembroService membroService;

    public CadastroService(IgrejaService igrejaService, MembroService membroService) {
        this.igrejaService = igrejaService;
        this.membroService = membroService;
    }

    public List<IgrejaSemiCompletoDTO> buscarIgrejas(){
        List<IgrejaSemiCompletoDTO> igrejas = igrejaService.buscarIgrejas();
        return igrejas;
    }



    public RestResponseMessage cadastrarMembro(CadastroExternoDTO cadastroDTO){
        Membro membro = membroService.criarMembroExterno(cadastroDTO);

        return new RestResponseMessage(HttpStatus.CREATED, "Usuário cadastrado com sucesso");
    }


}
