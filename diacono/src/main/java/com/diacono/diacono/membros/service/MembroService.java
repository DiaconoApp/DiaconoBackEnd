package com.diacono.diacono.membros.service;

import com.diacono.diacono.membros.dto.MembrosCreateRequestDTO;
import com.diacono.diacono.membros.dto.MembrosResponseDTO;
import com.diacono.diacono.membros.entity.Membro;
import com.diacono.diacono.membros.repository.MembrosRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MembroService {

    private final MembrosRepository membrosRepository;


    public MembroService(MembrosRepository membrosRepository) {
        this.membrosRepository = membrosRepository;
    }

    public MembrosResponseDTO criar (MembrosCreateRequestDTO membroDTO){
        Membro membro = new Membro(membroDTO.nome(), membroDTO.email(), membroDTO.dataNascimento(),
                membroDTO.cpf(), membroDTO.cep(), membroDTO.numeroCasa(),
                membroDTO.senhaTemporaria(), membroDTO.senha());

        Membro salvo = membrosRepository.save(membro);

        return new MembrosResponseDTO(salvo.getNome());

    }


}
