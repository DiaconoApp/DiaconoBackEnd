package com.diacono.diacono.usecases.igreja;

import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.repository.IgrejaRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarIgrejaPorUUIDUseCase {

    private final IgrejaRepository igrejaRepository;

    public BuscarIgrejaPorUUIDUseCase(IgrejaRepository igrejaRepository) {
        this.igrejaRepository = igrejaRepository;
    }

    /*ESSE MÉTODO SE RELACIONA COM EVENTO*/
    public Igreja execute(UUID idExterno){
        //adicionar validação da existência da Igreja -- SE DER ERRO LANÇAR EXCEÇÃO
        Igreja igreja = igrejaRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ObjectNotFoundException("Igreja não encontrada"));;

        return igreja;
    }
}
