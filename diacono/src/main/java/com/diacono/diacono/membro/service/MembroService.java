package com.diacono.diacono.membro.service;

import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.membro.exceptions.MembroNaoEncontradoException;
import com.diacono.diacono.membro.mapper.MembroMapper;
import com.diacono.diacono.membro.model.dto.request.MembroCreateDTO;
import com.diacono.diacono.membro.model.dto.response.MembroResponseDTO;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.repository.MembroRepository;
import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MembroService {

    private final MembroRepository membroRepository;
//    private final EnderecoMembroMapper enderecoMembroMapper;
    private final MembroMapper membroMapper;
    private final PasswordEncoder passwordEncoder;
//    private final MinisteriosRepository ministerioRepository;

    public MembroService(MembroRepository membroRepository, MembroMapper membroMapper, PasswordEncoder passwordEncoder) {
        this.membroRepository = membroRepository;
        this.membroMapper = membroMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<MembroResponseDTO> buscarTodosSemFiltro(Pageable pageable) {

        Page<Membro> membros = buscaMembros(pageable, null);
        Page<MembroResponseDTO> response = membroMapper.paraMembrosResponseDTO(membros);
        validaResponsePage(response);

        return response;
    }

    @Transactional(readOnly = true)
    public List<MembroResponseDTO> buscarTodosComFiltro(
            Pageable pageable,
            String termoBusca,
            EnumStatusMembro status,
            UUID fkMinisterio ) {

        Page<Membro> membrosPage = buscaMembros(pageable, termoBusca);
        List<Membro> membros = membrosPage.getContent();

        if(status == null && fkMinisterio == null){
            List<MembroResponseDTO> response = membroMapper.paraMembrosResponseDTO(membros);
            validaResponseList(response);
            return response;
        }

        List<Membro> membrosFiltradosList = membros.stream()
                .filter(membro -> {

                    boolean passaNoFiltro = true;
                    if (passaNoFiltro && status != null) {
                        if (!membro.getStatus().equals(status)) {
                            passaNoFiltro = false;
                        }
                    }

                    if (passaNoFiltro && fkMinisterio != null) {
                        if (!fkMinisterio.equals(membro.getIdExterno())) {
                            passaNoFiltro = false;
                        }
                    }

                    return passaNoFiltro;
                })
                .collect(Collectors.toList());

        List<MembroResponseDTO> response = membroMapper.paraMembrosResponseDTO(membrosFiltradosList);
        validaResponseList(response);
        return response;

    }

    public RestResponseMessage criarMembro(MembroCreateDTO membroDTO){

        //dois cenários possíveis:
        //membro com fkMinisterio preenchido
        //membro com fkMinisterio nulo

        if(membroDTO.ministerios() == null || membroDTO.ministerios().isEmpty()){
            return criarMembroSemMinisterio(membroDTO);
        }

        return criarMembroComMinisterio(membroDTO);
    }

    // METODOS AUXILIARES

    private RestResponseMessage criarMembroSemMinisterio(MembroCreateDTO membroDTO){

        if(membroDTO.cargo().equals(EnumCargoMembro.LIDER_MINISTERIO)){
            throw new ObjectSaveErrorException("Para cadastrar um líder de ministério, é necessário associar um ministério ao membro.");
        }

        Membro membro = membroMapper.paraMembro(membroDTO);
        membro.setSenha(hashSenha(membroDTO.senha()));
        Membro membroSalvo = membroRepository.save(membro);
        validaCriacao(membroSalvo);

        return new RestResponseMessage(HttpStatus.CREATED, "Usuário cadastrado com sucesso");
    }

    private RestResponseMessage criarMembroComMinisterio(MembroCreateDTO membroDTO){
        //IMPLEMENTAR
        return null;
    }

    private void validaCriacao(Membro membro){
        if(membro == null){
            throw new ObjectSaveErrorException("Não foi possível cadastrar o usuário");
        }
    }

    private Page<Membro> buscaMembros(Pageable pageable, String busca){

        if (busca == null || busca.isBlank()){
            Page<Membro> membros = membroRepository.findAll(pageable);
            validarMembrosEncontrados(membros);
            return membros;
        }

        String buscaFormatada = "%"+busca+"%";
        Page<Membro> membros = membroRepository.findAllWithFilter(pageable, buscaFormatada);
        validarMembrosEncontrados(membros);

        return membros;
    }

    private void validarMembrosEncontrados(Page<Membro> membros){
        if(membros.isEmpty()){
            throw new ObjectNotFoundException("Nenhum membro encontrado");
        }
    }

    private void validaResponseList(List<MembroResponseDTO> membros){
        if(membros == null || membros.isEmpty()){
            throw new ObjectNotFoundException("Não foi possível converter para DTOs");
        }
    };

    private void validaResponsePage(Page<MembroResponseDTO> response){
        if(response.isEmpty()){
            throw new ObjectNotFoundException("Não foi possível converter para DTOs");
        }
    };

    public String hashSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    /*MÉTODO QUE SE RELACIONA COM A ENTIDADE EVENTO*/

    public Membro buscarPorUUID(UUID idExterno){
        /*FAZER VALIDAÇÃO DE PRESENÇA -- LANÇAR EXCEÇÃO*/
        Membro membro = membroRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new MembroNaoEncontradoException("Membro não encontrado com ID: " + idExterno));

        return membro;
    }
}
