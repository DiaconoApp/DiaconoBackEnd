package com.diacono.diacono.membro.service;

import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.membro.exceptions.MembroNaoEncontradoException;
import com.diacono.diacono.membro.exceptions.MinisterioNaoEncontradoException;
import com.diacono.diacono.membro.mapper.EnderecoMembroMapper;
import com.diacono.diacono.membro.mapper.MembroMapper;
import com.diacono.diacono.membro.model.dto.*;
import com.diacono.diacono.membro.model.entity.EnderecoMembro;
import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.model.entity.MembroMinisterio;
import com.diacono.diacono.membro.repository.MembrosRepository;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import com.diacono.diacono.ministerio.repository.MinisteriosRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MembroService {

    private final MembrosRepository membrosRepository;
    private final EnderecoMembroMapper enderecoMembroMapper;
    private final MembroMapper membroMapper;
    private final PasswordEncoder passwordEncoder;
    private final MinisteriosRepository ministerioRepository;

    public MembroService(MembrosRepository membrosRepository, EnderecoMembroMapper enderecoMembroMapper, MembroMapper membroMapper, PasswordEncoder passwordEncoder, MinisteriosRepository ministerioRepository) {
        this.membrosRepository = membrosRepository;
        this.enderecoMembroMapper = enderecoMembroMapper;
        this.membroMapper = membroMapper;
        this.passwordEncoder = passwordEncoder;
        this.ministerioRepository = ministerioRepository;
    }

    @Transactional
    public MembrosResponseDTO criar (MembrosCreateDTO membroDTO){

        EnderecoMembro enderecoMembro = enderecoMembroMapper.paraEnderecoMembro(membroDTO.membroEnderecoDTO());

        String senha = hashSenha(membroDTO.senha());

        Membro membro = membroMapper.paraMembro(membroDTO);
        membro.setEnderecoMembro(enderecoMembro);
        membro.setSenha(senha);

        if (membroDTO.ministerio() != null && !membroDTO.ministerio().isEmpty()) {

            List<UUID> ministerioIds = membroDTO.ministerio().stream()
                    .map(MinisterioMembroCreateDTO::idExterno)
                    .toList();

            List<Ministerio> ministeriosEncontrados = ministerioRepository.findAllByIdExterno(ministerioIds);

            if (ministeriosEncontrados.size() != ministerioIds.size()) {

                List<UUID> encontradosIds = ministeriosEncontrados.stream()
                        .map(Ministerio::getIdExterno)
                        .toList();
                List<UUID> faltantes = ministerioIds.stream()
                        .filter(id -> !encontradosIds.contains(id))
                        .toList();
                throw new MinisterioNaoEncontradoException("Ministérios não encontrados: " + faltantes);
            }

            membro.setMinisterio(ministeriosEncontrados);
        }

        Membro salvo = membrosRepository.save(membro);

        return new MembrosResponseDTO(salvo.getNome(), salvo.getEmail(), salvo.getCelular(), salvo.getDataNascimento(), salvo.getMinisterio(), salvo.getStatusMembro());
    }

    @Transactional
    public RestResponseMessage atualizar(UUID idExterno, MembrosUpdateDTO updateDTO) {

        Membro membro = membrosRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new MembroNaoEncontradoException(
                        "Membro não encontrado com ID: " + idExterno));

        if (updateDTO.nome() != null) {
            membro.setNome(updateDTO.nome());
        }

        if (updateDTO.cpf() != null) {
            membro.setCpf(updateDTO.cpf());
        }

        if (updateDTO.dataNascimento() != null) {
            membro.setDataNascimento(updateDTO.dataNascimento());
        }

        if (updateDTO.email() != null) {
            membro.setEmail(updateDTO.email());
        }

        if (updateDTO.celular() != null) {
            membro.setCelular(updateDTO.celular());
        }

        if (updateDTO.membroEnderecoDTO() != null) {
            atualizarEndereco(membro, updateDTO.membroEnderecoDTO());
        }

        // Atualizar ministérios com cargos específicos
        if (updateDTO.ministerio() != null) {
            atualizarMinisterios(membro, updateDTO.ministerio());
        }

        membrosRepository.save(membro);

        return new RestResponseMessage(HttpStatus.OK, "Membro atualizado com sucesso");
    }

    private void atualizarEndereco(Membro membro, EnderecoMembroDTO enderecoDTO) {
        EnderecoMembro endereco = membro.getEnderecoMembro();

        if (endereco == null) {

            endereco = enderecoMembroMapper.paraEnderecoMembro(enderecoDTO);
            membro.setEnderecoMembro(endereco);
        } else {

            if (enderecoDTO.cep() != null) {
                endereco.setCep(enderecoDTO.cep());
            }
            if (enderecoDTO.estado() != null) {
                endereco.setEstado(enderecoDTO.estado());
            }
            if (enderecoDTO.cidade() != null) {
                endereco.setCidade(enderecoDTO.cidade());
            }
            if (enderecoDTO.bairro() != null) {
                endereco.setBairro(enderecoDTO.bairro());
            }
            if (enderecoDTO.rua() != null) {
                endereco.setRua(enderecoDTO.rua());
            }
            if (enderecoDTO.numero() != null) {
                endereco.setNumero(enderecoDTO.numero());
            }
            if (enderecoDTO.complemento() != null) {
                endereco.setComplemento(enderecoDTO.complemento());
            }
        }
    }

    private void atualizarMinisterios(Membro membro, List<MinisterioMembroCreateDTO> ministeriosDTO) {

        membro.getMinisterio().clear();

        if (ministeriosDTO.isEmpty()) {
            return;
        }

        ministeriosDTO.forEach(ministerioDTO -> {

            Ministerio ministerio = ministerioRepository.findByIdExterno(ministerioDTO.idExterno())
                    .orElseThrow(() -> new MinisterioNaoEncontradoException(
                            "Ministério não encontrado: " + ministerioDTO.idExterno()));

            // Define o cargo (usa o informado ou MEMBRO como padrão)
            EnumCargoMembro cargo = ministerioDTO.cargo() != null ?
                    ministerioDTO.cargo() :
                    EnumCargoMembro.MEMBRO;

            MembroMinisterio membroMinisterio = MembroMinisterio.builder()
                    .membro(membro)
                    .ministerio(ministerio)
                    .cargoMembro(cargo)
                    .build();

            membro.getMinisterio().add(membroMinisterio);
        });
    }

    @Transactional
    public RestResponseMessage adicionarMinisterio(UUID membroId, UUID ministerioId, EnumCargoMembro cargo) {

        Membro membro = membrosRepository.findByIdExterno(membroId)
                .orElseThrow(() -> new MembroNaoEncontradoException("Membro não encontrado"));

        Ministerio ministerio = ministerioRepository.findByIdExterno(ministerioId)
                .orElseThrow(() -> new MinisterioNaoEncontradoException("Ministério não encontrado"));

        // Verifica se já participa
        boolean jaParticipa = membro.getMinisterio().stream()
                .anyMatch(mm -> mm.getMinisterio().getIdExterno().equals(ministerioId));

        if (jaParticipa) {
            return new RestResponseMessage(HttpStatus.BAD_REQUEST,
                    "Membro já participa deste ministério");
        }

        // Adiciona sem remover os outros
        MembroMinisterio membroMinisterio = MembroMinisterio.builder()
                .membro(membro)
                .ministerio(ministerio)
                .cargoMembro(cargo != null ? cargo : EnumCargoMembro.MEMBRO)
                .build();

        membro.getMinisterio().add(membroMinisterio);
        membrosRepository.save(membro);

        return new RestResponseMessage(HttpStatus.OK, "Ministério adicionado com sucesso");
    }

    @Transactional
    public RestResponseMessage atualizarCargoMinisterio(UUID membroId, UUID ministerioId, EnumCargoMembro novoCargo) {

        Membro membro = membrosRepository.findByIdExterno(membroId)
                .orElseThrow(() -> new MembroNaoEncontradoException("Membro não encontrado"));

        MembroMinisterio membroMinisterio = membro.getMinisterio().stream()
                .filter(mm -> mm.getMinisterio().getIdExterno().equals(ministerioId))
                .findFirst()
                .orElseThrow(() -> new MinisterioNaoEncontradoException(
                        "Membro não participa deste ministério"));

        membroMinisterio.setCargoMembro(novoCargo);
        membrosRepository.save(membro);

        return new RestResponseMessage(HttpStatus.OK,
                "Cargo atualizado para " + novoCargo);
    }

    @Transactional(readOnly = true)
    public Page<MembrosResponseDTO> getAll(Pageable pageable) {
        return membrosRepository.findAll(pageable)
                .map(membro -> new MembrosResponseDTO(
                        membro.getNome(),
                        membro.getEmail(),
                        membro.getCelular(),
                        membro.getDataNascimento(),
                        membro.getMinisterio(),
                        membro.getStatusMembro()
                ));
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

    public String hashSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    /*MÉTODO QUE SE RELACIONA COM A ENTIDADE EVENTO*/

    public Optional<Membro> buscarPorUUID(UUID idExterno){
        /*FAZER VALIDAÇÃO DE PRESENÇA -- LANÇAR EXCEÇÃO*/
        Optional<Membro> membro = membrosRepository.findByIdExterno(idExterno);

        if(membro == null){
            throw new ObjectNotFoundException("Membro não encontrado");
        }

        return membro;
    }
}
