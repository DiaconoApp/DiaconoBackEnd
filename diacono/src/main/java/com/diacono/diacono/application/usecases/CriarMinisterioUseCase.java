package com.diacono.diacono.application.usecases;

import com.diacono.diacono.domain.entities.Membro;
import com.diacono.diacono.domain.entities.MembroMinisterio;
import com.diacono.diacono.domain.entities.Ministerio;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.persistence.MembroRepository;
import com.diacono.diacono.presentation.dto.MinisterioCreateDTO;
import com.diacono.diacono.presentation.dto.response.RestResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class CriarMinisterioUseCase {

    private final MembroRepository repository;

    public CriarMinisterioUseCase(MembroRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RestResponseMessage criarMinisterio(MinisterioCreateDTO ministerioDTO) {

        //buscar lider primeiro para adicionar como membro do ministério
        Membro liderMinisterio = repository.findByIdExterno(ministerioDTO.idLider());

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
        repository.save(novoMinisterio);

        return new RestResponseMessage(HttpStatus.CREATED, "Ministério criado com sucesso");

    }

}
