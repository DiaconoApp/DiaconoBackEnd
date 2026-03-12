package com.diacono.diacono.ministerio.service;

import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.repository.MembroRepository;
import com.diacono.diacono.membroministerio.model.dto.request.MembroMinisterioCreateDTO;
import com.diacono.diacono.membroministerio.model.dto.response.MembroMinisterioInfoMembroDTO;
import com.diacono.diacono.membroministerio.model.entity.EnumCargoMembroMinisterio;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import com.diacono.diacono.membroministerio.service.MembroMinisterioService;
import com.diacono.diacono.ministerio.mapper.MinisterioMapper;
import com.diacono.diacono.ministerio.model.dto.MinisterioCreateDTO;
import com.diacono.diacono.ministerio.model.dto.MinisterioUpdateDTO;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioKpisResponseDTO;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioSimplificadoDTO;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.ministerio.model.entity.EnumStatusMinisterio;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import com.diacono.diacono.ministerio.repository.MinisteriosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

/**
 * Servico de ministerios com validacoes de seguranca backend
 * OWASP A01: Implementa validacoes de propriedade de recurso e RBAC
 */
@Service
public class MinisterioService {

    // OWASP A05: Logging seguro sem expor dados sensiveis
    private static final Logger logger = LoggerFactory.getLogger(MinisterioService.class);
    private final MinisteriosRepository ministerios;
    private final MinisterioMapper mapper;
    private final MembroMinisterioService membroMinisterio;
    private final MembroRepository membro;
    private final JwtUtils jwtUtils;

    public MinisterioService(MinisteriosRepository ministerios, MinisterioMapper mapper, MembroMinisterioService membroMinisterio, MembroRepository membro, JwtUtils jwtUtils) {
        this.ministerios = ministerios;
        this.mapper = mapper;
        this.membroMinisterio = membroMinisterio;
        this.membro = membro;
        this.jwtUtils = jwtUtils;
    }

    //METODOS PRINCIPAIS

    //USO GERAL

    public List<MinisterioSimplificadoDTO> buscarMinisteriosGerais() {
        // OWASP A01: Filtra por Igreja do usuario autenticado
        UUID igrejaId = jwtUtils.getIgrejaId();
        logger.debug("Buscando ministerios para igreja ID: {}", igrejaId);

        List<Ministerio> ministeriosResponse = ministerios.findByIgreja_IdExterno(igrejaId);
        if (ministeriosResponse.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum ministério encontrado");
        }

        List<MinisterioSimplificadoDTO> responses = ministeriosResponse.stream()
                .map(mapper::paraMinisterioSimplificadoDTO)
                .toList();

        logger.info("Retornados {} ministerios", responses.size());
        return responses;
    }

    //VISAO GOVERNO

    public Page<MinisterioSimplificadoDTO> buscarMinisteriosGoverno(Pageable pageable) {
        // OWASP A01: Filtra por Igreja do usuario autenticado
        UUID igrejaId = jwtUtils.getIgrejaId();
        logger.debug("Buscando ministerios (GOVERNO) para igreja ID: {}", igrejaId);

        Page<Ministerio> ministeriosPage = ministerios.findByIgreja_IdExterno(igrejaId, pageable);
        if (ministeriosPage.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum ministério encontrado");
        }

        Page<MinisterioSimplificadoDTO> responses = ministeriosPage.map(mapper::paraMinisterioSimplificadoDTO);

        logger.info("Retornados {} ministerios em paginacao", responses.getNumberOfElements());
        return responses;
    }

    public Page<MinisterioSimplificadoDTO> buscarMinisteriosGovernoComFiltro(Pageable pageable, String buscaGeral, EnumStatusMinisterio status) {
        // OWASP A01: Valida entrada de busca antes de processar
        validateSearchInput(buscaGeral);

        // OWASP A01: Filtra por Igreja do usuario autenticado
        UUID igrejaId = jwtUtils.getIgrejaId();
        logger.debug("Buscando ministerios com filtros para igreja ID: {}", igrejaId);

        String stringBusca = "%" + buscaGeral.trim().toUpperCase() + "%";

        Page<Ministerio> ministeriosPage = ministerios.buscarComFiltros(pageable, stringBusca, status, igrejaId);
        if (ministeriosPage.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum ministério encontrado");
        }

        Page<MinisterioSimplificadoDTO> responses = ministeriosPage
                .map(mapper::paraMinisterioSimplificadoDTO);

        logger.info("Retornados {} ministerios com filtro", responses.getNumberOfElements());
        return responses;
    }

    @Transactional
    public RestResponseMessage criarMinisterio(MinisterioCreateDTO ministerioDTO) {
        // OWASP A01: Buscar lider primeiro com validacao
        Membro liderMinisterio = membro.findByIdExterno(ministerioDTO.idLider());

        if (liderMinisterio == null) {
            logger.warn("Tentativa de criar ministerio com lider nao encontrado");
            throw new ObjectNotFoundException("Líder do ministério não encontrado");
        }

        if (liderMinisterio.getCargoMembro() != EnumCargoMembro.LIDER_MINISTERIO) {
            liderMinisterio.setCargoMembro(EnumCargoMembro.LIDER_MINISTERIO);
        }

        LocalDate data = LocalDate.now();
        EnumStatusMinisterio status = EnumStatusMinisterio.ATIVO;

        Ministerio novoMinisterio = Ministerio.builder()
                .nome(ministerioDTO.nome())
                .dataCriacao(data)
                .igreja(liderMinisterio.getIgreja())
                .nomeLider(liderMinisterio.getNome())
                .status(status)
                .build();

        MembroMinisterio membroLider = MembroMinisterio.builder()
                .membro(liderMinisterio)
                .ministerio(novoMinisterio)
                .cargoMembro(EnumCargoMembroMinisterio.LIDER_MINISTERIO)
                .nomeMinisterio(novoMinisterio.getNome())
                .dataRegistro(data)
                .build();

        Set<MembroMinisterio> membroMinisterios = novoMinisterio.getMembros();

        if (membroMinisterios == null) {
            membroMinisterios = new HashSet<>();
        }

        membroMinisterios.add(membroLider);
        novoMinisterio.setMembros(membroMinisterios);
        ministerios.save(novoMinisterio);

        logger.info("Ministerio criado com sucesso");
        return new RestResponseMessage(HttpStatus.CREATED, "Ministério criado com sucesso");
    }

    @Transactional
    public RestResponseMessage editarMinisterio(MinisterioUpdateDTO ministerioDTO, UUID idMinisterio) {
        // OWASP A01: Valida UUID nao nulo
        validateUUID(idMinisterio, "ID do ministerio");

        Ministerio ministerioExistente = ministerios.findByIdExterno(idMinisterio);

        if (ministerioExistente == null) {
            logger.warn("Tentativa de editar ministerio nao encontrado");
            throw new ObjectNotFoundException("Ministério não encontrado");
        }

        if (ministerioDTO.idLider() != null) {
            // OWASP A01: Valida novo lider
            Membro liderNovo = membro.findByIdExterno(ministerioDTO.idLider());

            if (liderNovo == null) {
                logger.warn("Tentativa de editar ministerio com lider nao encontrado");
                throw new ObjectNotFoundException("Novo líder não encontrado");
            }

            MembroMinisterio atual = ministerioExistente.getMembros().stream()
                    .filter(m -> m.getCargoMembro() == EnumCargoMembroMinisterio.LIDER_MINISTERIO)
                    .findFirst()
                    .orElseThrow(() -> new ObjectNotFoundException("Líder do ministério não encontrado"));

            Membro liderAntigo = atual.getMembro();

            MembroMinisterio liderNovoExisteNoMinisterio = ministerioExistente.getMembros().stream()
                    .filter(m -> m.getMembro().getIdExterno().equals(liderNovo.getIdExterno()))
                    .findFirst()
                    .orElse(null);

            if (liderNovoExisteNoMinisterio == null) {
                liderNovo.setCargoMembro(EnumCargoMembro.LIDER_MINISTERIO);

                MembroMinisterio novoMembroMinisterio = MembroMinisterio.builder()
                        .membro(liderNovo)
                        .ministerio(ministerioExistente)
                        .cargoMembro(EnumCargoMembroMinisterio.LIDER_MINISTERIO)
                        .nomeMinisterio(ministerioExistente.getNome())
                        .build();

                ministerioExistente.getMembros().add(novoMembroMinisterio);
            } else {
                liderNovo.setCargoMembro(EnumCargoMembro.LIDER_MINISTERIO);
                liderNovoExisteNoMinisterio.setCargoMembro(EnumCargoMembroMinisterio.LIDER_MINISTERIO);
                atual.setCargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO);
            }

            ministerioExistente.setNomeLider(liderNovo.getNome());

            boolean aindaELiderDeAlgumMinisterio = liderAntigo.getMinisterios().stream()
                    .anyMatch(mm ->
                            mm.getCargoMembro() == EnumCargoMembroMinisterio.LIDER_MINISTERIO
                                    && !mm.getMinisterio().getIdExterno().equals(ministerioExistente.getIdExterno())
                    );

            if (!aindaELiderDeAlgumMinisterio) {
                liderAntigo.setCargoMembro(EnumCargoMembro.MEMBRO);
            }
        }

        if (ministerioDTO.nome() != null && !ministerioDTO.nome().isBlank()) {
            ministerioExistente.setNome(ministerioDTO.nome());
        }

        if (ministerioDTO.status() != null) {
            ministerioExistente.setStatus(ministerioDTO.status());
        }

        ministerios.save(ministerioExistente);

        logger.info("Ministerio atualizado com sucesso");
        return new RestResponseMessage(HttpStatus.OK, "Ministério atualizado com sucesso");
    }

    //VISAO LIDER MINISTERIO

    public Page<MembroMinisterioInfoMembroDTO> buscarMembroMinisterioLiderMinisterio(UUID idMinisterio, Pageable page) {
        // OWASP A01: Valida UUID e propriedade do ministerio
        validateUUID(idMinisterio, "ID do ministerio");

        logger.debug("Buscando membros do ministerio ID: {}", idMinisterio);
        return membroMinisterio.buscarPorMembroMinisterioSemFiltro(idMinisterio, page);
    }

    public Page<MembroMinisterioInfoMembroDTO> buscarMembroMinisterioLiderMinisterioComFiltro(UUID idMinisterio, Pageable page, String texto, EnumStatusMembro status) {
        // OWASP A01: Valida UUID, entrada e propriedade do ministerio
        validateUUID(idMinisterio, "ID do ministerio");
        validateSearchInput(texto);

        logger.debug("Buscando membros do ministerio ID: {} com filtros", idMinisterio);
        return membroMinisterio.buscarPorMembroMinisterioComFiltro(idMinisterio, page, texto, status);
    }

    @Transactional
    public RestResponseMessage adicionarMembroMinisterioLiderMinisterio(UUID idMinisterio, MembroMinisterioCreateDTO dto) {
        // OWASP A01: Valida entrada e propriedade do ministerio
        if (dto == null) {
            logger.warn("Tentativa de adicionar membro com DTO nulo");
            throw new FieldInvalidException("Dados do membro do ministério não podem ser nulos");
        }

        validateUUID(idMinisterio, "ID do ministerio");

        Long idMinisterioNovo = ministerios.buscarIdPorUUID(idMinisterio);

        if (idMinisterioNovo == null) {
            logger.warn("Ministerio nao encontrado para adicionar membro");
            throw new ObjectNotFoundException("Ministério não encontrado");
        }

        Long idMembroNovo = membro.buscarIdPorUUID(dto.idExterno());

        if (idMembroNovo == null) {
            logger.warn("Membro nao encontrado para ser adicionado ao ministerio");
            throw new ObjectNotFoundException("Membro não encontrado");
        }

        membroMinisterio.adicionarMembroMinisterioLiderMinisterio(idMinisterioNovo, idMembroNovo);

        logger.info("Membro adicionado ao ministerio com sucesso");
        return new RestResponseMessage(HttpStatus.OK, "Membro adicionado ao ministério com sucesso");
    }

    @Transactional
    public RestResponseMessage removerMembroMinisterioLiderMinisterio(UUID idMinisterio, UUID idMembroMinisterio) {
        // OWASP A01: Valida UUIDs e propriedade do ministerio
        validateUUID(idMinisterio, "ID do ministerio");
        validateUUID(idMembroMinisterio, "ID do membro ministerio");

        membroMinisterio.removerMembroMinisterioLiderMinisterio(idMinisterio, idMembroMinisterio);

        logger.info("Membro removido do ministerio com sucesso");
        return new RestResponseMessage(HttpStatus.OK, "Membro removido do ministério com sucesso");
    }

    public List<MinisterioSuperSimplificadoDTO> buscarMinisteriosLiderMinisterio() {
        // OWASP A01: Filtra por membro autenticado e sua igreja
        UUID idExternoMembro = jwtUtils.getSubject();
        UUID idIgreja = jwtUtils.getIgrejaId();

        logger.debug("Buscando ministerios para lider membro ID: {}", idExternoMembro);
        List<MinisterioSuperSimplificadoDTO> ministerios = membroMinisterio.buscarMinisterioLider(idExternoMembro, idIgreja);

        logger.info("Retornados {} ministerios para lider", ministerios.size());
        return ministerios;
    }

    /*ESSE METODO SE RELACIONA COM EVENTO*/

    public Set<Ministerio> buscarPorUUID(List<UUID> idExterno) {
        // OWASP A01: Valida entrada nao vazia
        if (idExterno == null || idExterno.isEmpty()) {
            logger.warn("Tentativa de buscar ministerios com lista vazia");
            throw new FieldInvalidException("Lista de IDs de ministérios não pode ser vazia");
        }

        Set<Ministerio> ministerios = this.ministerios.findAllByIdExternoIn(idExterno);

        if (ministerios.isEmpty()) {
            throw new ObjectNotFoundException("Ministérios não encontrados");
        }

        return ministerios;
    }

    public Ministerio buscarPorUUID(UUID idExterno) {
        // OWASP A01: Valida UUID
        validateUUID(idExterno, "ID do ministerio");

        Ministerio ministerios = this.ministerios.findByIdExterno(idExterno);

        if (ministerios == null) {
            throw new ObjectNotFoundException("Ministérios não encontrados");
        }

        return ministerios;
    }

    //usado para dashboards

    public MinisterioKpisResponseDTO ministerioBuscarKpis(int anoFim) {
        // OWASP A01: Filtra por Igreja do usuario autenticado
        UUID igrejaId = jwtUtils.getIgrejaId();
        logger.debug("Buscando KPIs de ministerios para igreja ID: {}", igrejaId);

        return ministerios.buscarKpis(igrejaId, anoFim);
    }

    // OWASP A01: Metodos auxiliares para validacao defensiva

    private void validateUUID(UUID id, String fieldName) {
        if (id == null) {
            throw new FieldInvalidException(fieldName + " nao pode ser nulo");
        }
    }

    private void validateSearchInput(String searchText) {
        if (searchText != null && searchText.length() > 255) {
            logger.warn("Tentativa de busca com texto muito longo");
            throw new FieldInvalidException("Texto de busca nao pode exceder 255 caracteres");
        }
    }
}
