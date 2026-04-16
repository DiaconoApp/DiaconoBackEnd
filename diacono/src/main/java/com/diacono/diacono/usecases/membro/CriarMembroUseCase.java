package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.membro.MembroCreateDTO;
import com.diacono.diacono.applications.mappers.membro.MembroMapper;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectExistsException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.usecases.igreja.BuscarIgrejaPorUUIDUseCase;
import com.diacono.diacono.usecases.membro.validation.ValidarCriacaoMembro;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class CriarMembroUseCase {

    private final MembroRepository membroRepository;
    private final MembroMapper membroMapper;
    private final ValidarCriacaoMembro validarCriacaoMembro;
    private final MinisteriosRepository ministeriosRepository;
    private final MembroMinisterioRepository membroMinisterioRepository;
    private final BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase;

    public CriarMembroUseCase(MembroRepository membroRepository, MembroMapper membroMapper, ValidarCriacaoMembro validarCriacaoMembro, MinisteriosRepository ministeriosRepository, MembroMinisterioRepository membroMinisterioRepository, BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase) {
        this.membroRepository = membroRepository;
        this.membroMapper = membroMapper;
        this.validarCriacaoMembro = validarCriacaoMembro;
        this.ministeriosRepository = ministeriosRepository;
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.buscarIgrejaPorUUIDUseCase = buscarIgrejaPorUUIDUseCase;
    }

    @Transactional
    public RestResponseMessageDTO execute(MembroCreateDTO membroDTO) {

        if (membroDTO == null) {
            throw new ObjectSaveErrorException("Dados do membro não podem ser nulos.");
        }

        if (membroDTO.idExternoMinisterios() == null) {
            Membro response = criarMembroSemMinisterio(membroDTO);
            return new RestResponseMessageDTO(HttpStatus.CREATED, "Usuário cadastrado com sucesso");
        }

        return criarMembroComMinisterio(membroDTO);
    }

    private Membro criarMembroSemMinisterio(MembroCreateDTO membroDTO) {

        if (membroDTO.cargo().equals(EnumCargoMembro.LIDER_MINISTERIO) && (membroDTO.idExternoMinisterios() == null)) {
            throw new ObjectSaveErrorException("Para cadastrar um líder de ministério, é necessário associar um ministério ao membro.");
        }

        Optional<Membro> membroExistente = membroRepository.findByEmailOrCpf(membroDTO.email(), membroDTO.cpf());

        if(membroExistente.isPresent()){
            throw new ObjectExistsException("Email ou CPF ja cadastrado");
        }

        LocalDate dataHoje = LocalDate.now();

        Membro membro = membroMapper.paraMembro(membroDTO);
        Igreja igreja = buscarIgrejaPorUUIDUseCase.execute(membroDTO.fkIgreja());
        membro.setStatus(EnumStatusMembro.ATIVO);
        membro.setIgreja(igreja);
        membro.setDataRegistro(dataHoje);
        membro.setGeneroMembro(membroDTO.generoMembro());
        membro.setCargoMembro(membroDTO.cargo());
        membro.setSenha(validarCriacaoMembro.hashSenha(membroDTO.senha()));
        Membro membroSalvo = membroRepository.save(membro);
        validarCriacaoMembro.validaCriacao(membroSalvo);

        return membroSalvo;
    }

    private RestResponseMessageDTO criarMembroComMinisterio(MembroCreateDTO membroDTO) {
        Ministerio ministerios = buscarPorUUID(membroDTO.idExternoMinisterios());

        Membro membro = criarMembroSemMinisterio(membroDTO);
        apagarMembroMinisterioPorMembro(membro);

        MembroMinisterio membroMinisterio = MembroMinisterio.builder()
                .membro(membro)
                .ministerio(ministerios)
                .cargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO)
                .nomeMinisterio(ministerios.getNome())
                .build();

        salvarTodos(membroMinisterio);

        return new RestResponseMessageDTO(HttpStatus.CREATED, "Usuário cadastrado com sucesso");

    }

    public Ministerio buscarPorUUID(UUID idExterno) {
        Ministerio ministerios = this.ministeriosRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ObjectNotFoundException("Ministério não encontrado"));

        return ministerios;
    }

    public void apagarMembroMinisterioPorMembro(Membro membro){
        int count = membroMinisterioRepository.deleteByMembro(membro);

    }

    public void salvarTodos(MembroMinisterio membrosMinisterios){

        MembroMinisterio membroMinisterios = membroMinisterioRepository.save(membrosMinisterios);

        if(membroMinisterios == null || membroMinisterios.getIdInterno() == null){
            throw new ObjectSaveErrorException("Nenhum membro_ministerio foi salvo.");
        }
    }
}