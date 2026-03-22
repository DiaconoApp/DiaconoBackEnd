package com.diacono.diacono.application.usecases;

import com.diacono.diacono.domain.entities.Membro;
import com.diacono.diacono.domain.entities.MembroMinisterio;
import com.diacono.diacono.domain.entities.Ministerio;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.persistence.MembroRepository;
import com.diacono.diacono.infrastructure.persistence.MinisteriosRepository;
import com.diacono.diacono.presentation.dto.MinisterioUpdateDTO;
import com.diacono.diacono.presentation.dto.response.RestResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EditarMinisterioUseCase {

    private final MinisteriosRepository repository;
    private final MembroRepository membroRepository;

    public EditarMinisterioUseCase(MinisteriosRepository repository, MembroRepository membroRepository) {
        this.repository = repository;
        this.membroRepository = membroRepository;
    }

    @Transactional
    public RestResponseMessage editarMinisterio(MinisterioUpdateDTO ministerioDTO, UUID idMinisterio) {

        Ministerio ministerioExistente = repository.findByIdExterno(idMinisterio);


        if (ministerioExistente == null) {
            throw new ObjectNotFoundException("Ministério não encontrado");
        }

        if (ministerioDTO.idLider() != null) {


            Membro liderNovo = membroRepository.findByIdExterno(ministerioDTO.idLider());

            if (liderNovo == null) {
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


        repository.save(ministerioExistente);

        return new RestResponseMessage(HttpStatus.OK, "Ministério atualizado com sucesso");
    }
}
