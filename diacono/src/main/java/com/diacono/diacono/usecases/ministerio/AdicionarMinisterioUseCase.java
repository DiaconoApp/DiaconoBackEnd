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
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.infrastructure.persistence.springdata.MembroJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class AdicionarMinisterioUseCase {

    private static final Logger log = LoggerFactory.getLogger(AdicionarMinisterioUseCase.class);

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
    public RestResponseMessageDTO execute(MinisterioCreateDTO ministerioDTO, UUID igrejaIdToken) {

        Membro liderMinisterio = membroRepository.findByIdExterno(ministerioDTO.idLider());

        if (liderMinisterio == null) {
            log.warn("Tentativa de criar ministério com líder inexistente. idLider=[{}]", ministerioDTO.idLider());
            throw new ObjectNotFoundException("Líder do ministério não encontrado");
        }

        if (liderMinisterio.getIgreja() == null || !liderMinisterio.getIgreja().getIdExterno().toString().equals(igrejaIdToken)) {
            log.warn("Tentativa de criar ministério com líder de outra igreja. idLider=[{}] igrejaToken=[{}]",
                    ministerioDTO.idLider(), igrejaIdToken);
            throw new ObjectNotFoundException("Líder do ministério não encontrado");
        }

        if (liderMinisterio.getCargoMembro() != EnumCargoMembro.LIDER_MINISTERIO) {
            log.info("Promovendo membro a líder de ministério. membroId=[{}]", ministerioDTO.idLider());
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

        log.info("Ministério criado com sucesso. nome=[{}] igrejaId=[{}]", ministerioDTO.nome(), igrejaIdToken);

        return new RestResponseMessageDTO(HttpStatus.CREATED, "Ministério criado com sucesso");
    }
}