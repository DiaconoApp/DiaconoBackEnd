package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.mappers.MembroMapper;
import com.diacono.diacono.domain.entities.Membro;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.MembroRepository;
import com.diacono.diacono.presentation.dto.response.MembroResponseDTO;

@Service
public class BuscarTodosSemFiltroUseCase {

    private final MembroRepository repository;
    private final MembroMapper mapper;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public BuscarTodosSemFiltroUseCase(MembroRepository repository, MembroMapper mapper, JwtClaimsExtractor jwtClaimsExtractor) {
        this.repository = repository;
        this.mapper = mapper;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    @Transactional(readOnly = true)
    public Page<MembroResponseDTO> buscarTodosSemFiltro(Pageable pageable) {

        Page<Membro> membrosPage = repository.findByIgreja_IdExterno(jwtClaimsExtractor.getIgrejaId(),pageable);
        validarMembrosEncontradosPage(membrosPage);

        return membrosPage.map(mapper::paraMembroResponseDTO);
    }

    private void validarMembrosEncontradosPage(Page<Membro> membros) {
        if (membros.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum membro encontrado");
        }
    }
}
