package com.diacono.diacono.cadastro.service;

import com.diacono.diacono.Igreja.model.dto.response.IgrejaSemiCompletoDTO;
import com.diacono.diacono.Igreja.service.IgrejaService;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CadastroService {

    private final IgrejaService igrejaService;

    public CadastroService(IgrejaService igrejaService) {
        this.igrejaService = igrejaService;
    }

    public List<IgrejaSemiCompletoDTO> buscarIgrejas(){
        List<IgrejaSemiCompletoDTO> igrejas = igrejaService.buscarIgrejas();
        if(igrejas == null || igrejas.isEmpty()){
            throw new ObjectNotFoundException("Igrejas não encontradas");
        }
        return igrejas;
    }


    public void cadastrarMembro(){

    }


}
