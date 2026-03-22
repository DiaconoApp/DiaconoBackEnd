package com.diacono.diacono.application.usecases;

import com.diacono.diacono.domain.entities.Membro;
import com.diacono.diacono.domain.entities.MembroMinisterio;
import com.diacono.diacono.domain.entities.Ministerio;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.application.exceptions.FieldInvalidException;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.application.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.infrastructure.persistence.MembroMinisterioRepository;
import com.diacono.diacono.infrastructure.persistence.MembroRepository;
import com.diacono.diacono.infrastructure.persistence.MinisteriosRepository;
import com.diacono.diacono.presentation.dto.request.MembroMinisterioCreateDTO;
import com.diacono.diacono.presentation.dto.response.RestResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class AdicionarLiderMinisterioUseCase {

    private final MinisteriosRepository repository;
    private final MembroRepository membroRepository;
    private final MembroMinisterioRepository membroMinisterioRepository;

    public AdicionarLiderMinisterioUseCase(MinisteriosRepository repository, MembroRepository membroRepository, MembroMinisterioRepository membroMinisterioRepository) {
        this.repository = repository;
        this.membroRepository = membroRepository;
        this.membroMinisterioRepository = membroMinisterioRepository;
    }

    @Transactional
    public RestResponseMessage adicionarMembroMinisterioLiderMinisterio(UUID idMinisterio, MembroMinisterioCreateDTO dto) {

        if (dto == null) {
            throw new FieldInvalidException("Dados do membro do ministério não podem ser nulos");
        }

        Long idMinisterioNovo = repository.buscarIdPorUUID(idMinisterio);

        if (idMinisterioNovo == null) {
            throw new ObjectNotFoundException("Ministério não encontrado");
        }

        Long idMembroNovo = membroRepository.buscarIdPorUUID(dto.idExterno());

        if (idMembroNovo == null) {
            throw new ObjectNotFoundException("Membro não encontrado");
        }

        adicionarMembroMinisterioLiderMinisterio(idMinisterioNovo, idMembroNovo);

        return new RestResponseMessage(HttpStatus.OK, "Membro adicionado ao ministério com sucesso");
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
