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
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.infrastructure.persistence.springdata.MembroJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AdicionarMembroMinisterioLiderMinisterioUseCase {

    private final MinisteriosRepository ministeriosRepository;
    private final MembroJpaRepository membroRepository;
    private final MembroMinisterioRepository membroMinisterioRepository;
    private final JwtUtils jwtUtils;

    public AdicionarMembroMinisterioLiderMinisterioUseCase(MinisteriosRepository ministeriosRepository, MembroJpaRepository membroRepository, MembroMinisterioRepository membroMinisterioRepository, JwtUtils jwtUtils) {
        this.ministeriosRepository = ministeriosRepository;
        this.membroRepository = membroRepository;
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public RestResponseMessageDTO execute(UUID idMinisterio, MembroMinisterioCreateDTO dto, UUID igrejaIdToken, UUID membroIdToken) {

        if (dto == null) {
            throw new FieldInvalidException("Dados do membro do ministério não podem ser nulos");
        }

        Long idMinisterioNovo = ministeriosRepository.buscarIdPorUUID(idMinisterio)
                .orElseThrow(() -> new ObjectNotFoundException("Ministério não encontrado"));

        Ministerio ministerioVerificado = ministeriosRepository.findByIdExterno(idMinisterio)
                .orElseThrow(() -> new ObjectNotFoundException("Ministério não encontrado"));

        if (!ministerioVerificado.getIgreja().getIdExterno().equals(igrejaIdToken)) {
            throw new ObjectNotFoundException("Ministério não encontrado para a igreja do usuário");
        }

        validarLiderMinisterio(membroIdToken, igrejaIdToken, idMinisterio);

        Long idMembroNovo = membroRepository.buscarIdPorUUID(dto.idExterno());

        if (idMembroNovo == null) {
            throw new ObjectNotFoundException("Membro não encontrado");
        }

        adicionarMembroMinisterioLiderMinisterio(idMinisterioNovo, idMembroNovo);

        return new RestResponseMessageDTO(HttpStatus.OK, "Membro adicionado ao ministério com sucesso");
    }

    private void validarLiderMinisterio(UUID membroId, UUID igrejaId, UUID ministerioId) {
        List<MinisterioSuperSimplificadoDTO> ministériosLider = membroMinisterioRepository
                .buscarMinisterioLider(membroId, igrejaId);

        if (ministériosLider.isEmpty() || ministériosLider.stream()
                .noneMatch(m -> m.idExterno().equals(ministerioId))) {
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