package com.diacono.diacono.membro.service;

import com.diacono.diacono.membro.model.dto.MembrosCreateDTO;
import com.diacono.diacono.membro.model.dto.MembrosResponseDTO;
import com.diacono.diacono.membro.model.dto.MembrosUpdateDTO;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.repository.MembrosRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MembroService {

    private final MembrosRepository membrosRepository;


    public MembroService(MembrosRepository membrosRepository) {
        this.membrosRepository = membrosRepository;
    }

    public MembrosResponseDTO criar (MembrosCreateDTO membroDTO){
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

    public MembrosResponseDTO updateMembro(MembrosUpdateDTO membroDTO, Integer id){

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


    /*MÉTODO QUE SE RELACIONA COM A ENTIDADE EVENTO*/

    public Membro buscarPorUUID(UUID idExterno){
        /*FAZER VALIDAÇÃO DE PRESENÇA -- LANÇAR EXCEÇÃO*/
        return membrosRepository.findByIdExterno(idExterno);

    }

}
