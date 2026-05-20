package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.membro.MembroUpdateDTO;
import com.diacono.diacono.domain.entity.EnderecoMembro;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AtualizarMembroUseCase {

    private static final Logger logger = LoggerFactory.getLogger(AtualizarMembroUseCase.class);

    private final MembroRepository membroRepository;
    private final MembroMinisterioRepository membroMinisterioRepository;
    private final MinisteriosRepository ministeriosRepository;
    private final EscalaMinisterioRepository escalaMinisterioRepository;

    public AtualizarMembroUseCase(
            MembroRepository membroRepository,
            MembroMinisterioRepository membroMinisterioRepository,
            MinisteriosRepository ministeriosRepository,
            EscalaMinisterioRepository escalaMinisterioRepository
    ) {
        this.membroRepository = membroRepository;
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.ministeriosRepository = ministeriosRepository;
        this.escalaMinisterioRepository = escalaMinisterioRepository;
    }

    @Transactional
    public RestResponseMessageDTO execute(UUID idExterno, MembroUpdateDTO request, UUID igrejaId) {
        Membro membro = membroRepository.findByIdExternoAndIgrejaIdExterno(idExterno, igrejaId)
            .orElseThrow(() -> new ObjectNotFoundException("Membro não encontrado na igreja autenticada"));

        if (request.fkIgreja() != null && !request.fkIgreja().equals(igrejaId)) {
            logger.warn("Tentativa de alterar Igreja do membro fora do escopo. membroId=[{}] igrejaId=[{}] fkIgreja_tentativa=[{}]",
                idExterno, igrejaId, request.fkIgreja());
            throw new ObjectSaveErrorException("Alteração de Igreja não é permitida");
        }

        if (request.nome() != null) membro.setNome(request.nome());
        if (request.cpf() != null) membro.setCpf(request.cpf());
        if (request.email() != null) membro.setEmail(request.email());
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

        logger.info("Membro atualizado com sucesso. membroId=[{}] igrejaId=[{}] campos_atualizados=[{}]",
            idExterno, igrejaId, obterCamposAlterados(request));

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
        Set<UUID> idsUnicos = new LinkedHashSet<>(idsExternosMinisterio);
        List<MembroMinisterio> vinculosAtuais = membroMinisterioRepository
                .findAllByMembroIdExternoAndIgrejaIdExterno(membro.getIdExterno(), membro.getIgreja().getIdExterno());

        Map<UUID, MembroMinisterio> vinculosPorMinisterioId = vinculosAtuais.stream()
                .filter(vinculo -> vinculo.getMinisterio() != null && vinculo.getMinisterio().getIdExterno() != null)
                .collect(Collectors.toMap(
                        vinculo -> vinculo.getMinisterio().getIdExterno(),
                        vinculo -> vinculo,
                        (existente, duplicado) -> existente,
                        HashMap::new
                ));

        List<MembroMinisterio> vinculosRemovidos = vinculosAtuais.stream()
                .filter(vinculo -> vinculo.getMinisterio() != null)
                .filter(vinculo -> vinculo.getMinisterio().getIdExterno() != null)
                .filter(vinculo -> !idsUnicos.contains(vinculo.getMinisterio().getIdExterno()))
                .toList();

        if (!vinculosRemovidos.isEmpty()) {
            escalaMinisterioRepository.deleteByMembroMinisterioIdsAndIgrejaId(
                    membro.getIgreja().getIdExterno(),
                    vinculosRemovidos.stream()
                            .map(MembroMinisterio::getIdExterno)
                            .toList()
            );

            for (MembroMinisterio vinculoRemovido : vinculosRemovidos) {
                membroMinisterioRepository.deleteByMembroIdExternoAndMinisterioIdExterno(
                        membro.getIdExterno(),
                        vinculoRemovido.getMinisterio().getIdExterno()
                );
            }
            membroMinisterioRepository.flush();
        }

        if (idsUnicos.isEmpty()) {
            return;
        }

        Set<Ministerio> ministerios = ministeriosRepository.findAllByIdExternoInAndIgrejaId(
                new ArrayList<>(idsUnicos),
                membro.getIgreja().getIdExterno()
        );

        if (ministerios.size() != idsUnicos.size()) {
            throw new ObjectNotFoundException("Um ou mais ministérios não foram encontrados");
        }

        for (Ministerio ministerio : ministerios) {
            if (vinculosPorMinisterioId.containsKey(ministerio.getIdExterno())) {
                continue;
            }

            MembroMinisterio membroMinisterio = MembroMinisterio.builder()
                    .membro(membro)
                    .ministerio(ministerio)
                    .cargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO)
                    .nomeMinisterio(ministerio.getNome())
                    .build();

            membroMinisterioRepository.save(membroMinisterio);
        }
    }

    private String obterCamposAlterados(MembroUpdateDTO request) {
        List<String> campos = new ArrayList<>();
        if (request.nome() != null) campos.add("nome");
        if (request.cpf() != null) campos.add("cpf");
        if (request.email() != null) campos.add("email");
        if (request.celular() != null) campos.add("celular");
        if (request.dataNascimento() != null) campos.add("dataNascimento");
        if (request.cargo() != null) campos.add("cargo");
        if (request.generoMembro() != null) campos.add("generoMembro");
        if (request.status() != null) campos.add("status");
        if (request.membroEnderecoDTO() != null) campos.add("endereco");
        if (request.idExternoMinisterios() != null) campos.add("ministerios");
        return String.join(",", campos);
    }
}
