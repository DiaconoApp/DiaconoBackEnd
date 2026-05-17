package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioUpdateDTO;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.persistence.springdata.MembroJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EditarMinisterioUseCase {

    private static final Logger logger = LoggerFactory.getLogger(EditarMinisterioUseCase.class);

    private final MinisteriosRepository ministeriosRepository;
    private final MembroJpaRepository membroRepository;

    public EditarMinisterioUseCase(
            MinisteriosRepository ministeriosRepository,
            MembroJpaRepository membroRepository
    ) {
        this.ministeriosRepository = ministeriosRepository;
        this.membroRepository = membroRepository;
    }

    @Transactional
    public RestResponseMessageDTO execute(MinisterioUpdateDTO ministerioDTO, UUID idMinisterio, UUID  igrejaIdToken) {

        Ministerio ministerioExistente = ministeriosRepository.findByIdExterno(idMinisterio)
                .orElseThrow(() -> new ObjectNotFoundException("Ministério não encontrado"));

        if (ministerioExistente.getIgreja() == null || !ministerioExistente.getIgreja().getIdExterno().equals(igrejaIdToken)) {
            logger.warn("Tentativa de editar ministério de outra igreja. ministerioId=[{}] igrejaToken=[{}]",
                    idMinisterio, igrejaIdToken);
            throw new ObjectNotFoundException("Ministério não encontrado");
        }

        if (ministerioDTO.idLider() != null) {
            atualizarLider(ministerioExistente, ministerioDTO.idLider(), igrejaIdToken);
        }

        if (ministerioDTO.nome() != null && !ministerioDTO.nome().isBlank()) {
            ministerioExistente.setNome(ministerioDTO.nome());
        }

        if (ministerioDTO.status() != null) {
            ministerioExistente.setStatus(ministerioDTO.status());
        }

        ministeriosRepository.save(ministerioExistente);

        logger.info("Ministério atualizado com sucesso. ministerioId=[{}] igrejaId=[{}]", idMinisterio, igrejaIdToken);

        return new RestResponseMessageDTO(HttpStatus.OK, "Ministério atualizado com sucesso");
    }

    private void atualizarLider(Ministerio ministerioExistente, UUID idLiderNovo, UUID igrejaIdToken) {

        Membro liderNovo = membroRepository.findByIdExterno(idLiderNovo);

        if (liderNovo == null) {
            logger.warn("Tentativa de definir líder inexistente. idLiderNovo=[{}]", idLiderNovo);
            throw new ObjectNotFoundException("Novo líder não encontrado");
        }

        if (liderNovo.getIgreja() == null || !liderNovo.getIgreja().getIdExterno().equals(igrejaIdToken)) {
            logger.warn("Tentativa de definir líder de outra igreja. idLiderNovo=[{}] igrejaToken=[{}]",
                    idLiderNovo, igrejaIdToken);
            throw new ObjectNotFoundException("Novo líder não encontrado");
        }

        MembroMinisterio liderAtual = ministerioExistente.getMembros().stream()
                .filter(m -> m.getCargoMembro() == EnumCargoMembroMinisterio.LIDER_MINISTERIO)
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Líder do ministério não encontrado"));

        Membro liderAntigo = liderAtual.getMembro();

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
            liderAtual.setCargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO);
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
}