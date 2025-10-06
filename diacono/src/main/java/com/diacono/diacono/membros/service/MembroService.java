package com.diacono.diacono.membros.service;

import com.diacono.diacono.membros.model.dto.EnderecoMembroDTO;
import com.diacono.diacono.membros.model.dto.MembrosCreateDTO;
import com.diacono.diacono.membros.model.dto.MembrosResponseDTO;
import com.diacono.diacono.membros.model.dto.MembrosUpdateDTO;
import com.diacono.diacono.membros.model.entity.EnderecoMembro;
import com.diacono.diacono.membros.model.entity.Membro;
import com.diacono.diacono.membros.repository.MembrosRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembroService {

    private final MembrosRepository membrosRepository;

    public MembroService(MembrosRepository membrosRepository) {
        this.membrosRepository = membrosRepository;
    }

    public MembrosResponseDTO criar (MembrosCreateDTO membroDTO){

        EnderecoMembro endereco = new EnderecoMembro(
                membroDTO.membroEnderecoDTO().cep(),
                membroDTO.membroEnderecoDTO().estado(),
                membroDTO.membroEnderecoDTO().cidade(),
                membroDTO.membroEnderecoDTO().bairro(),
                membroDTO.membroEnderecoDTO().rua(),
                membroDTO.membroEnderecoDTO().numero(),
                membroDTO.membroEnderecoDTO().complemento()
        );

        Membro membro = new Membro(
                membroDTO.id(),
                membroDTO.nome(),
                membroDTO.cpf(),
                membroDTO.dataNascimento(),
                membroDTO.email(),
                membroDTO.celular(),
                membroDTO.senha(),
                membroDTO.ministerio(),
                membroDTO.status(),
                endereco
        );

        Membro salvo = membrosRepository.save(membro);

        return new MembrosResponseDTO(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getCelular(), salvo.getDataNascimento(), salvo.getMinisterio(), salvo.getAtivo());
    }

//    public List<MembrosResponseDTO> getAll (){
//
//        List<Membro> membros = membrosRepository.findAll();
//
//
//        if(membros.isEmpty()){
//            return null;
//        }
//        List<MembrosResponseDTO> membrosResponseDTO = membros.stream()
//                .map(m -> new MembrosResponseDTO(m.getId(), m.getNome(), m.getEmail(), m.getCelular(), m.getDataNascimento(), m.getMinisterio(), m.getAtivo()))
//                .toList();
//
//        return membrosResponseDTO;
//    }

    public Page<MembrosResponseDTO> getAll(Pageable pageable) {

        return membrosRepository.findAll(pageable)
                .map(membro -> new MembrosResponseDTO(membro.getId(),
                        membro.getNome(), membro.getEmail(),
                        membro.getCelular(), membro.getDataNascimento(),
                        membro.getMinisterio(), membro.getAtivo()));
    }

    public Long getMembroAtivos(){
        return membrosRepository.countMembroStatusIgualAtivo();
    }

    public Long getMembrosComMinisterio(){
        return membrosRepository.countMembrosComMinisterio();
    }

    public Long getCountMembrosDiscipulados() {
        return membrosRepository.countMembrosDiscipulados();
    }

//
//    public MembrosResponseDTO getForID (Integer id){
//
//        if(membrosRepository.existsById(id)){
//
//            Membro encontrado = membrosRepository.findById(id).get();
//            return new MembrosResponseDTO(encontrado.getNome(), encontrado.getDataNascimento(), encontrado.getId(), encontrado.getEmail());
//
//        }
//
//        return null;
//
//    }
//
//    public Boolean deleteMembro(Integer id){
//
//        if(membrosRepository.existsById(id)){
//
//            membrosRepository.deleteById(id);
//            return true;
//
//        }
//
//        return false;
//
//    }

//    public MembrosResponseDTO updateMembro(MembrosUpdateDTO membroDTO, Integer id){
//
//        if(membrosRepository.existsById(id)){
//
//            Membro encontrado = membrosRepository.findById(id).get();
//
//            if(membroDTO.email() != null){
//                encontrado.setEmail(membroDTO.email());
//            }
//
//            if(membroDTO.cep() != null){
//                encontrado.setCep(membroDTO.cep());
//            }
//
//            if(membroDTO.numeroCasa() != null){
//                encontrado.setNumeroCasa(membroDTO.numeroCasa());
//            }
//
//            if(membroDTO.senha() != null){
//                encontrado.setSenha(membroDTO.senha());
//            }
//
//            membrosRepository.save(encontrado);
//
//            return new MembrosResponseDTO(encontrado.getNome(), encontrado.getDataNascimento(), encontrado.getId(), encontrado.getEmail());
//        }
//
//        return null;
//
//    }


}
