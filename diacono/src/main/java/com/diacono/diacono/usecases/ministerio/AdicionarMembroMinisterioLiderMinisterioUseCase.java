package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioCreateDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class AdicionarMembroMinisterioLiderMinisterioUseCase {

    private final MinisteriosRepository ministeriosRepository;
    private final MembroJpaRepository membroRepository;
    private final MembroMinisterioRepository membroMinisterioRepository;

    public AdicionarMembroMinisterioLiderMinisterioUseCase(MinisteriosRepository ministeriosRepository, MembroJpaRepository membroRepository, MembroMinisterioRepository membroMinisterioRepository) {
        this.ministeriosRepository = ministeriosRepository;
        this.membroRepository = membroRepository;
        this.membroMinisterioRepository = membroMinisterioRepository;
    }

    @Transactional
    public RestResponseMessageDTO execute(UUID idMinisterio, MembroMinisterioCreateDTO dto) {

        if (dto == null) {
            throw new FieldInvalidException("Dados do membro do ministério não podem ser nulos");
        }

        Long idMinisterioNovo = ministeriosRepository.buscarIdPorUUID(idMinisterio)
                .orElseThrow(() -> new ObjectNotFoundException("Ministério não encontrado"));

        Long idMembroNovo = membroRepository.buscarIdPorUUID(dto.idExterno());

        if (idMembroNovo == null) {
            throw new ObjectNotFoundException("Membro não encontrado");
        }

        adicionarMembroMinisterioLiderMinisterio(idMinisterioNovo, idMembroNovo);

        return new RestResponseMessageDTO(HttpStatus.OK, "Membro adicionado ao ministério com sucesso");
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