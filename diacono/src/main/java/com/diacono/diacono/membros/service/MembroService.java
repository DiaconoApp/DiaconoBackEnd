package com.diacono.diacono.membros.service;

import com.diacono.diacono.membros.dto.MembrosCreateRequestDTO;
import com.diacono.diacono.membros.dto.MembrosResponseDTO;
import com.diacono.diacono.membros.dto.MembrosUpdateRequestDTO;
import com.diacono.diacono.membros.entity.Membro;
import com.diacono.diacono.membros.repository.MembrosRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        return new MembrosResponseDTO(salvo.getNome(), salvo.getDataNascimento(), salvo.getId(), salvo.getEmail());

    }

    public List<MembrosResponseDTO> getAll (){

        List<Membro> membros = membrosRepository.findAll();

        if(membros.isEmpty()){
            return null;
        }

        List<MembrosResponseDTO> membrosResponseDTOS = membros.stream()
                .map(m -> new MembrosResponseDTO(m.getNome(), m.getDataNascimento(), m.getId(), m.getEmail()))
                .toList();

        return membrosResponseDTOS;
    }

    public MembrosResponseDTO getForID (Integer id){

        if(membrosRepository.existsById(id)){

            Membro encontrado = membrosRepository.findById(id).get();
            return new MembrosResponseDTO(encontrado.getNome(), encontrado.getDataNascimento(), encontrado.getId(), encontrado.getEmail());

        }

        return null;

    }

    public Boolean deleteMembro(Integer id){

        if(membrosRepository.existsById(id)){

            membrosRepository.deleteById(id);
            return true;

        }

        return false;

    }

    public MembrosResponseDTO updateMembro(MembrosUpdateRequestDTO membroDTO, Integer id){

        if(membrosRepository.existsById(id)){

            Membro encontrado = membrosRepository.findById(id).get();

            if(membroDTO.email() != null){
                encontrado.setEmail(membroDTO.email());
            }

            if(membroDTO.cep() != null){
                encontrado.setCep(membroDTO.cep());
            }

            if(membroDTO.numeroCasa() != null){
                encontrado.setNumeroCasa(membroDTO.numeroCasa());
            }

            if(membroDTO.senha() != null){
                encontrado.setSenha(membroDTO.senha());
            }

            membrosRepository.save(encontrado);

            return new MembrosResponseDTO(encontrado.getNome(), encontrado.getDataNascimento(), encontrado.getId(), encontrado.getEmail());
        }

        return null;

    }


}
