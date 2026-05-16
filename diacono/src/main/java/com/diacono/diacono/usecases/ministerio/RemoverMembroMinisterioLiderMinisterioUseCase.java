package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoverMembroMinisterioLiderMinisterioUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RemoverMembroMinisterioLiderMinisterioUseCase.class);

    private final MembroMinisterioRepository membroMinisterioRepository;
    private final MinisteriosRepository ministeriosRepository;

    public RemoverMembroMinisterioLiderMinisterioUseCase(
            MembroMinisterioRepository membroMinisterioRepository,
            MinisteriosRepository ministeriosRepository
    ) {
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.ministeriosRepository = ministeriosRepository;
    }

    @Transactional
    public RestResponseMessageDTO execute(UUID idMinisterio, UUID idMembroMinisterio, UUID igrejaIdToken) {

        Ministerio ministerio = ministeriosRepository.findByIdExterno(idMinisterio)
                .orElseThrow(() -> new ObjectNotFoundException("Ministério não encontrado"));

        if (ministerio.getIgreja() == null || !ministerio.getIgreja().getIdExterno().equals(igrejaIdToken)) {
            logger.warn("Tentativa de remover membro de ministério de outra igreja. ministerioId=[{}] igrejaToken=[{}]",
                    idMinisterio, igrejaIdToken);
            throw new ObjectNotFoundException("Ministério não encontrado");
        }

        removerMembroMinisterioLiderMinisterio(idMinisterio, idMembroMinisterio, igrejaIdToken);

        return new RestResponseMessageDTO(HttpStatus.OK, "Membro removido do ministério com sucesso");
    }

    private void removerMembroMinisterioLiderMinisterio(UUID idMinisterio, UUID idMembro, UUID igrejaIdToken) {

        int count = membroMinisterioRepository.deleteByMembroIdExternoAndMinisterioIdExterno(idMembro, idMinisterio);

        if (count == 0) {
            logger.warn("Membro do ministério não encontrado para remoção. idMembro=[{}] ministerioId=[{}] igrejaId=[{}]",
                    idMembro, idMinisterio, igrejaIdToken);
            throw new ObjectNotFoundException("Membro do ministério não encontrado para remoção.");
        }

        logger.info("Membro removido do ministério com sucesso. idMembro=[{}] ministerioId=[{}] igrejaId=[{}]",
                idMembro, idMinisterio, igrejaIdToken);
    }
}