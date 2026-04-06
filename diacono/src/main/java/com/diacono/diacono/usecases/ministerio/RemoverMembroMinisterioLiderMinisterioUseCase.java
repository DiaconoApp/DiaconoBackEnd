package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoverMembroMinisterioLiderMinisterioUseCase {

    private final MembroMinisterioRepository membroMinisterioRepository;

    public RemoverMembroMinisterioLiderMinisterioUseCase(MembroMinisterioRepository membroMinisterioRepository) {
        this.membroMinisterioRepository = membroMinisterioRepository;
    }

    @Transactional
    public RestResponseMessageDTO execute(UUID idMinisterio, UUID idMembroMinisterio) {

        removerMembroMinisterioLiderMinisterio(idMinisterio, idMembroMinisterio);

        return new RestResponseMessageDTO(HttpStatus.OK, "Membro removido do ministério com sucesso");
    }

    private void removerMembroMinisterioLiderMinisterio(UUID idMinisterio, UUID idMembro){

        int count = membroMinisterioRepository.deleteByMembroIdExternoAndMinisterioIdExterno(idMembro, idMinisterio);

        if(count == 0){
            throw new ObjectNotFoundException("Membro do ministério não encontrado para remoção.");
        }

    }

}