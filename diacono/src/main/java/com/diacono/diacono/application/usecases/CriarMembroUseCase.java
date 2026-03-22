package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.mappers.MembroMapper;
import com.diacono.diacono.application.service.IgrejaFetcher;
import com.diacono.diacono.application.service.MinisteriosFetcher;
import com.diacono.diacono.domain.entities.Igreja;
import com.diacono.diacono.domain.entities.Membro;
import com.diacono.diacono.domain.entities.MembroMinisterio;
import com.diacono.diacono.domain.entities.Ministerio;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.application.exceptions.ObjectExistsException;
import com.diacono.diacono.application.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.infrastructure.persistence.MembroMinisterioRepository;
import com.diacono.diacono.infrastructure.persistence.MembroRepository;
import com.diacono.diacono.presentation.dto.request.MembroCreateDTO;
import com.diacono.diacono.presentation.dto.response.RestResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.diacono.diacono.application.validators.MembroValidator.validaCriacao;

@Service
public class CriarMembroUseCase {

    private final MembroRepository repository;
    private final MinisteriosFetcher ministeriosFetcher;
    private final IgrejaFetcher igrejaFetcher;
    private final MembroMapper mapper;
    private final MembroMinisterioRepository membroMinisterioRepository;

    public CriarMembroUseCase(MembroRepository repository, MinisteriosFetcher ministeriosFetcher, IgrejaFetcher igrejaFetcher, MembroMapper mapper, MembroMinisterioRepository membroMinisterioRepository) {
        this.repository = repository;
        this.ministeriosFetcher = ministeriosFetcher;
        this.igrejaFetcher = igrejaFetcher;
        this.mapper = mapper;
        this.membroMinisterioRepository = membroMinisterioRepository;
    }

    @Transactional
    public RestResponseMessage criarMembro(MembroCreateDTO membroDTO) {

        if (membroDTO == null) {
            throw new ObjectSaveErrorException("Dados do membro não podem ser nulos.");
        }

        if (membroDTO.idExternoMinisterios() == null) {
            Membro response = criarMembroSemMinisterio(membroDTO);
            return new RestResponseMessage(HttpStatus.CREATED, "Usuário cadastrado com sucesso");
        }

        return criarMembroComMinisterio(membroDTO);
    }

    // METODOS AUXILIARES


    private Membro criarMembroSemMinisterio(MembroCreateDTO membroDTO) {

        if (membroDTO.cargo().equals(EnumCargoMembro.LIDER_MINISTERIO) && (membroDTO.idExternoMinisterios() == null)) {
            throw new ObjectSaveErrorException("Para cadastrar um líder de ministério, é necessário associar um ministério ao membro.");
        }

        Membro membroExistente = repository.findByEmailOrCpf(membroDTO.email(), membroDTO.cpf());

        if(membroExistente != null){
            throw new ObjectExistsException("Email ou CPF ja cadastrado");
        }

        LocalDate dataHoje = LocalDate.now();

        Membro membro = mapper.paraMembro(membroDTO);
        Igreja igreja = igrejaFetcher.buscarUUID(membroDTO.fkIgreja());
        membro.setStatus(EnumStatusMembro.ATIVO);
        membro.setIgreja(igreja);
        membro.setDataRegistro(dataHoje);
        membro.setGeneroMembro(membroDTO.generoMembro());
        membro.setCargoMembro(membroDTO.cargo());
        membro.setSenha(hashSenha(membroDTO.senha()));
        Membro membroSalvo = repository.save(membro);
        validaCriacao(membroSalvo);

        return membroSalvo;

    }


    private RestResponseMessage criarMembroComMinisterio(MembroCreateDTO membroDTO) {
        Ministerio ministerios = ministeriosFetcher.buscarPorUUID(membroDTO.idExternoMinisterios());

        Membro membro = criarMembroSemMinisterio(membroDTO);
        membroMinisterioRepository.deleteByMembro(membro);

        MembroMinisterio membroMinisterio = MembroMinisterio.builder()
                .membro(membro)
                .ministerio(ministerios)
                .cargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO)
                .nomeMinisterio(ministerios.getNome())
                .build();

        salvarTodos(membroMinisterio);

        return new RestResponseMessage(HttpStatus.CREATED, "Usuário cadastrado com sucesso");

    }

    private void salvarTodos(MembroMinisterio membroMinisterio){
        MembroMinisterio membroMinisterios = membroMinisterioRepository.save(membroMinisterio);

        if(membroMinisterios == null || membroMinisterios.getIdInterno() == null){
            throw new ObjectSaveErrorException("Nenhum membro_ministerio foi salvo.");
        }
    }


}
