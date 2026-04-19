package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.membro.MembroUpdateDTO;
import com.diacono.diacono.domain.entity.EnderecoMembro;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.usecases.igreja.BuscarIgrejaPorUUIDUseCase;
import com.diacono.diacono.usecases.membro.validation.ValidarCriacaoMembro;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class AtualizarMembroUseCase {

    private final MembroRepository membroRepository;
    private final MembroMinisterioRepository membroMinisterioRepository;
    private final MinisteriosRepository ministeriosRepository;
    private final BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase;
    private final ValidarCriacaoMembro validarCriacaoMembro;

    public AtualizarMembroUseCase(MembroRepository membroRepository,
                                  MembroMinisterioRepository membroMinisterioRepository,
                                  MinisteriosRepository ministeriosRepository,
                                  BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase,
                                  ValidarCriacaoMembro validarCriacaoMembro) {
        this.membroRepository = membroRepository;
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.ministeriosRepository = ministeriosRepository;
        this.buscarIgrejaPorUUIDUseCase = buscarIgrejaPorUUIDUseCase;
        this.validarCriacaoMembro = validarCriacaoMembro;
    }

    @Transactional
    public RestResponseMessageDTO execute(UUID idExterno, MembroUpdateDTO request) {
        Membro membro = membroRepository.findByIdExterno(idExterno)
            .orElseThrow(() -> new ObjectNotFoundException("Membro não encontrado"));

        if (request.fkIgreja() != null) membro.setIgreja(buscarIgrejaPorUUIDUseCase.execute(request.fkIgreja()));
        if (request.nome() != null) membro.setNome(request.nome().toLowerCase(Locale.ROOT));
        if (request.cpf() != null) membro.setCpf(request.cpf());
        if (request.email() != null) membro.setEmail(request.email().toLowerCase(Locale.ROOT));
        if (request.celular() != null) membro.setCelular(request.celular());
        if (request.dataNascimento() != null) membro.setDataNascimento(request.dataNascimento());
        if (request.cargo() != null) membro.setCargoMembro(request.cargo());
        if (request.generoMembro() != null) membro.setGeneroMembro(request.generoMembro());
        if (request.status() != null) membro.setStatus(request.status());

        if (request.membroEnderecoDTO() != null) {
            atualizarEnderecoMembro(membro, request);
        }

        if (request.idExternoMinisterios() != null) {
            atualizarMinisterioMembro(membro, request.idExternoMinisterios());
        }

        membroRepository.save(membro);
        return new RestResponseMessageDTO(HttpStatus.OK, "Membro atualizado com sucesso");
    }

    private void atualizarEnderecoMembro(Membro membro, MembroUpdateDTO request) {
        EnderecoMembro enderecoAtual = membro.getEnderecoMembro();
        if (enderecoAtual == null) {
            enderecoAtual = new EnderecoMembro();
            membro.setEnderecoMembro(enderecoAtual);
        }

        enderecoAtual.setCep(request.membroEnderecoDTO().cep());
        enderecoAtual.setEstado(request.membroEnderecoDTO().estado());
        enderecoAtual.setCidade(request.membroEnderecoDTO().cidade());
        enderecoAtual.setBairro(request.membroEnderecoDTO().bairro());
        enderecoAtual.setRua(request.membroEnderecoDTO().rua());
        enderecoAtual.setComplemento(request.membroEnderecoDTO().complemento());

        try {
            enderecoAtual.setNumero(Integer.valueOf(request.membroEnderecoDTO().numero()));
        } catch (NumberFormatException e) {
            throw new ObjectSaveErrorException("Número do endereço inválido.");
        }
    }

    private void atualizarMinisterioMembro(Membro membro, List<UUID> idsExternosMinisterio) {
        membroMinisterioRepository.deleteByMembro(membro);
        membroMinisterioRepository.flush();

        if (idsExternosMinisterio.isEmpty()) {
            return;
        }

        Set<UUID> idsUnicos = new LinkedHashSet<>(idsExternosMinisterio);
        Set<Ministerio> ministerios = ministeriosRepository.findAllByIdExternoIn(new ArrayList<>(idsUnicos));

        if (ministerios.size() != idsUnicos.size()) {
            throw new ObjectNotFoundException("Um ou mais ministérios não foram encontrados");
        }

        for (Ministerio ministerio : ministerios) {
            MembroMinisterio membroMinisterio = MembroMinisterio.builder()
                    .membro(membro)
                    .ministerio(ministerio)
                    .cargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO)
                    .nomeMinisterio(ministerio.getNome())
                    .build();

            membroMinisterioRepository.save(membroMinisterio);
        }
    }
}