package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.CadastroExternoDTO;
import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.mappers.membro.MembroMapper;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectExistsException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.usecases.igreja.BuscarIgrejaPorUUIDUseCase;
import com.diacono.diacono.usecases.membro.validation.ValidarCriacaoMembro;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CadastrarMembroUseCase {

    private final MembroRepository membroRepository;
    private final MembroMapper membroMapper;
    private final BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase;
    private final ValidarCriacaoMembro validarCriacaoMembro;

    public CadastrarMembroUseCase(MembroRepository membroRepository, MembroMapper membroMapper, BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase, ValidarCriacaoMembro validarCriacaoMembro) {
        this.membroRepository = membroRepository;
        this.membroMapper = membroMapper;
        this.buscarIgrejaPorUUIDUseCase = buscarIgrejaPorUUIDUseCase;
        this.validarCriacaoMembro = validarCriacaoMembro;
    }

    public RestResponseMessageDTO execute(CadastroExternoDTO cadastroDTO) {

        Membro membro = criarMembroExterno(cadastroDTO);

        return new RestResponseMessageDTO(HttpStatus.CREATED, "Usuário cadastrado com sucesso");
    }

    public Membro criarMembroExterno(CadastroExternoDTO membroDTO) {

        if(membroDTO == null){
            throw new ObjectSaveErrorException("Dados do membro não podem ser nulos.");
        }

        Membro membroExistente = membroRepository.findByEmailOrCpf(membroDTO.email(), membroDTO.cpf())
                .orElseThrow(() -> new ObjectNotFoundException("Membro não encontrado"));

        if(membroExistente != null){
            throw new ObjectExistsException("Erro ao se cadastrar");
        }

        LocalDate dataHoje = LocalDate.now();

        Membro membro = membroMapper.paraMembro(membroDTO);
        Igreja igreja = buscarIgrejaPorUUIDUseCase.execute(membroDTO.fkIgreja());
        membro.setStatus(EnumStatusMembro.ATIVO);
        membro.setIgreja(igreja);
        membro.setGeneroMembro(membroDTO.generoMembro());
        membro.setCargoMembro(EnumCargoMembro.MEMBRO);
        membro.setDataRegistro(dataHoje);
        membro.setSenha(validarCriacaoMembro.hashSenha(membroDTO.senha()));
        Membro membroSalvo = membroRepository.save(membro);
        validarCriacaoMembro.validaCriacao(membroSalvo);

        return membroSalvo;

    }
}