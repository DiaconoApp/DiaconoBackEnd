package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.mappers.MembroMapper;
import com.diacono.diacono.application.service.IgrejaFetcher;
import com.diacono.diacono.domain.entities.Igreja;
import com.diacono.diacono.domain.entities.Membro;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.application.exceptions.ObjectExistsException;
import com.diacono.diacono.application.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.infrastructure.persistence.MembroRepository;
import com.diacono.diacono.presentation.dto.request.CadastroExternoDTO;
import com.diacono.diacono.presentation.dto.response.RestResponseMessage;

import java.time.LocalDate;

@Service
public class CadastrarIgrejaUseCase {

    private final MembroMapper mapper;
    private final MembroRepository repository;
    private final IgrejaFetcher igrejaFetcher;

    public CadastrarIgrejaUseCase(MembroMapper mapper, MembroRepository repository, IgrejaFetcher igrejaFetcher) {
        this.mapper = mapper;
        this.repository = repository;
        this.igrejaFetcher = igrejaFetcher;
    }

    public RestResponseMessage execute(CadastroExternoDTO cadastroDTO){
        if(cadastroDTO == null){
            throw new ObjectSaveErrorException("Dados do membro não podem ser nulos.");
        }

        Membro membroExistente = repository.findByEmailOrCpf(cadastroDTO.email(), cadastroDTO.cpf());

        if(membroExistente != null){
            throw new ObjectExistsException("Erro ao se cadastrar");
        }

        LocalDate dataHoje = LocalDate.now();

        Membro membro = mapper.paraMembro(cadastroDTO);
        Igreja igreja = igrejaFetcher.buscarUUID(cadastroDTO.fkIgreja());
        membro.setStatus(EnumStatusMembro.ATIVO);
        membro.setIgreja(igreja);
        membro.setGeneroMembro(membroDTO.generoMembro());
        membro.setCargoMembro(EnumCargoMembro.MEMBRO);
        membro.setDataRegistro(dataHoje);
        membro.setSenha(hashSenha(membroDTO.senha()));
        Membro membroSalvo = repository.save(membro);
        validaCriacao(membroSalvo);

        return new RestResponseMessage(HttpStatus.CREATED, "Usuário cadastrado com sucesso");
    }

    private void validaCriacao(Membro membro) {
        if (membro == null) {
            throw new ObjectSaveErrorException("Não foi possível cadastrar o usuário");
        }
    }

}
