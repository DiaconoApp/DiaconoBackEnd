package com.diacono.diacono.usecases;

import com.diacono.diacono.applications.dtos.igreja.IgrejaSemiCompletoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscasIgrejasUseCase {

    private final IgrejaService igrejaService;

    public BuscasIgrejasUseCase(IgrejaService igrejaService) {
        this.igrejaService = igrejaService;
    }

    public List<IgrejaSemiCompletoDTO> execute(){

        List<IgrejaSemiCompletoDTO> igrejas = igrejaService.buscarIgrejas();

        return igrejas;
    }
}
