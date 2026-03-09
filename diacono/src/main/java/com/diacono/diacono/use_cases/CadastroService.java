package com.diacono.diacono.use_cases;

import com.diacono.diacono.applications.dtos.igreja.IgrejaSemiCompletoDTO;
import com.diacono.diacono.applications.dtos.CadastroExternoDTO;
import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.entity.Membro;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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



    public RestResponseMessageDTO cadastrarMembro(CadastroExternoDTO cadastroDTO){
        Membro membro = membroService.criarMembroExterno(cadastroDTO);

        return new RestResponseMessageDTO(HttpStatus.CREATED, "Usuário cadastrado com sucesso");
    }


}
