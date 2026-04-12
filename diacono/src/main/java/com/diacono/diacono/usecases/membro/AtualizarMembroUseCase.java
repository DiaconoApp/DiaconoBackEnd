package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.membro.MembroUpdateDTO;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;

@Service
public class AtualizarMembroUseCase {

    private final MembroRepository membroRepository;

    public AtualizarMembroUseCase(MembroRepository membroRepository) {
        this.membroRepository = membroRepository;
    }

    public RestResponseMessageDTO execute(UUID idExterno, MembroUpdateDTO request) {
        Membro membro = membroRepository.findByIdExterno(idExterno)
            .orElseThrow(() -> new ObjectNotFoundException("Membro não encontrado"));

        if (request.nome() != null) membro.setNome(request.nome().toLowerCase(Locale.ROOT));
        if (request.email() != null) membro.setEmail(request.email().toLowerCase(Locale.ROOT));
        if (request.celular() != null) membro.setCelular(request.celular());
        if (request.dataNascimento() != null) membro.setDataNascimento(request.dataNascimento());
        if (request.cargo() != null) membro.setCargoMembro(request.cargo());

        membroRepository.save(membro);
        return new RestResponseMessageDTO(HttpStatus.OK, "Membro atualizado com sucesso");
    }
}