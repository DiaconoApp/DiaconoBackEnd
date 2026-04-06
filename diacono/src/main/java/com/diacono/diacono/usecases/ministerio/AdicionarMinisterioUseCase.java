package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioCreateDTO;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.persistence.Membro.MembroJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class AdicionarMinisterioUseCase {

    private final MinisteriosRepository ministeriosRepository;
    private final MembroJpaRepository membroRepository;

    public AdicionarMinisterioUseCase(
            MinisteriosRepository ministeriosRepository,
            MembroJpaRepository membroRepository
    ) {
        this.ministeriosRepository = ministeriosRepository;
        this.membroRepository = membroRepository;
    }

    @Transactional
    public RestResponseMessageDTO execute(MinisterioCreateDTO ministerioDTO) {

        Membro liderMinisterio = membroRepository.findByIdExterno(ministerioDTO.idLider());

        if (liderMinisterio == null) {
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
        ministeriosRepository.save(novoMinisterio);

        return new RestResponseMessageDTO(HttpStatus.CREATED, "Ministério criado com sucesso");
    }
}