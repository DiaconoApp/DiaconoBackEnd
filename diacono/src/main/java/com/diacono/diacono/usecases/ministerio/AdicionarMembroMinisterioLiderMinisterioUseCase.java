package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioCreateDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.infrastructure.persistence.springdata.MembroJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AdicionarMembroMinisterioLiderMinisterioUseCase {

    private static final Logger logger = LoggerFactory.getLogger(AdicionarMembroMinisterioLiderMinisterioUseCase.class);

    private final MinisteriosRepository ministeriosRepository;
    private final MembroJpaRepository membroRepository;
    private final MembroMinisterioRepository membroMinisterioRepository;

    public AdicionarMembroMinisterioLiderMinisterioUseCase(MinisteriosRepository ministeriosRepository, MembroJpaRepository membroRepository, MembroMinisterioRepository membroMinisterioRepository) {
        this.ministeriosRepository = ministeriosRepository;
        this.membroRepository = membroRepository;
        this.membroMinisterioRepository = membroMinisterioRepository;
    }

    @Transactional
    public RestResponseMessageDTO execute(UUID idMinisterio, MembroMinisterioCreateDTO dto, UUID igrejaIdToken, UUID membroIdToken) {

        if (dto == null) {
            logger.warn("Tentativa de adicionar membro com DTO nulo. membroId=[{}] igrejaId=[{}]", membroIdToken, igrejaIdToken);
            throw new FieldInvalidException("Dados do membro do ministério não podem ser nulos");
        }

        Long idMinisterioNovo = ministeriosRepository.buscarIdPorUUID(idMinisterio)
                .orElseThrow(() -> {
                    logger.warn("Ministério não encontrado no escopo da igreja autenticada. ministerioId=[{}] igrejaId=[{}] membroId=[{}]",
                            idMinisterio, igrejaIdToken, membroIdToken);
                    return new ObjectNotFoundException("Ministério não encontrado");
                });

        ministeriosRepository.findByIdExternoAndIgrejaId(idMinisterio, igrejaIdToken)
                .orElseThrow(() -> {
                    logger.warn("Ministério não encontrado no escopo da igreja autenticada (findByIdExternoAndIgrejaId). ministerioId=[{}] igrejaId=[{}] membroId=[{}]",
                            idMinisterio, igrejaIdToken, membroIdToken);
                    return new ObjectNotFoundException("Ministério não encontrado");
                });

        validarLiderMinisterio(membroIdToken, igrejaIdToken, idMinisterio);

        Membro membroVerificado = membroRepository.findByIdExterno(dto.idExterno());

        if (membroVerificado == null) {
            logger.warn("Tentativa de adicionar membro inexistente. membroIdAlvo=[{}] executor=[{}]", dto.idExterno(), membroIdToken);
            throw new ObjectNotFoundException("Membro não encontrado");
        }

        if (membroVerificado.getIgreja() == null || !membroVerificado.getIgreja().getIdExterno().equals(igrejaIdToken)) {
            logger.warn("Tentativa de adicionar membro de outra igreja ao ministério. membroIdAlvo=[{}] ministerioId=[{}] igrejaToken=[{}] executor=[{}]",
                    dto.idExterno(), idMinisterio, igrejaIdToken, membroIdToken);
            throw new ObjectNotFoundException("Membro não encontrado");
        }

        Long idMembroNovo = membroVerificado.getIdInterno();

        adicionarMembroMinisterioLiderMinisterio(idMinisterioNovo, idMembroNovo);

        logger.info("Membro adicionado ao ministério com sucesso. ministerioId=[{}] membroAdicionado=[{}] executor=[{}]",
                idMinisterio, dto.idExterno(), membroIdToken);

        return new RestResponseMessageDTO(HttpStatus.OK, "Membro adicionado ao ministério com sucesso");
    }

    private void validarLiderMinisterio(UUID membroId, UUID igrejaId, UUID ministerioId) {
        List<MinisterioSuperSimplificadoDTO> ministeriosLider = membroMinisterioRepository
                .buscarMinisterioLider(membroId, igrejaId);

        if (ministeriosLider.isEmpty() || ministeriosLider.stream()
                .noneMatch(m -> m.idExterno().equals(ministerioId))) {
            logger.warn("Tentativa de operação sem vínculo de liderança com o ministério. ministerioId=[{}] membroId=[{}] igrejaId=[{}]",
                    ministerioId, membroId, igrejaId);
            throw new ObjectNotFoundException("O líder informado não possui vínculo com o ministério solicitado");
        }
    }

    private void adicionarMembroMinisterioLiderMinisterio(Long idMinisterio, Long idMembro){

        Ministerio ministerio = Ministerio.builder()
                .idInterno(idMinisterio)
                .build();

        Membro membro = Membro.builder()
                .idInterno(idMembro)
                .build();

        LocalDate dataHoje = LocalDate.now();

        MembroMinisterio membroMinisterio = MembroMinisterio.builder()
                .ministerio(ministerio)
                .membro(membro)
                .cargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO)
                .nomeMinisterio(ministerio.getNome())
                .dataRegistro(dataHoje)
                .build();

        MembroMinisterio membroMinisterioSalvo = membroMinisterioRepository.save(membroMinisterio);

        if(membroMinisterioSalvo.getIdInterno() == null){
            throw new ObjectSaveErrorException("Membro do ministério não foi salvo.");
        }
    }
}