package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.mappers.MembroMapper;
import com.diacono.diacono.domain.entities.Membro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.MembroRepository;
import com.diacono.diacono.presentation.dto.response.MembroResponseDTO;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BuscarTodosComFiltroUseCase {

    private final MembroMapper mapper;
    private final MembroRepository repository;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public BuscarTodosComFiltroUseCase(MembroMapper mapper, MembroRepository repository, JwtClaimsExtractor jwtClaimsExtractor) {
        this.mapper = mapper;
        this.repository = repository;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    @Transactional(readOnly = true)
    public Page<MembroResponseDTO> buscarTodosComFiltro(
            Pageable pageable,
            String termoBusca,
            EnumStatusMembro status,
            UUID fkMinisterio) {

        //REFATORAR

        List<Membro> membrosBrutos = buscaMembros(termoBusca);

        List<Membro> membrosFiltrados = membrosBrutos.stream()
                .filter(membro -> {
                    boolean passaNoFiltro = true;

                    if (passaNoFiltro && status != null) {
                        if (membro.getStatus() == null || !membro.getStatus().equals(status)) {
                            passaNoFiltro = false;
                        }
                    }

                    if (passaNoFiltro && fkMinisterio != null) {
                        boolean pertenceAoMinisterio = membro.getMinisterios().stream()
                                .anyMatch(mm -> mm.getMinisterio().getIdExterno().equals(fkMinisterio));
                        if (!pertenceAoMinisterio) {
                            passaNoFiltro = false;
                        }
                    }else if(passaNoFiltro && (fkMinisterio == null)){
                        passaNoFiltro = true;
                    }

                    return passaNoFiltro;
                })
                .collect(Collectors.toList());

        validarMembrosEncontradosList(membrosFiltrados);

        int pageSize = pageable.getPageSize();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageSize), membrosFiltrados.size());

        List<Membro> contentListForPage;

        if (start > end) {
            contentListForPage = Collections.emptyList();
        } else {
            contentListForPage = membrosFiltrados.subList(start, end);
        }

        List<MembroResponseDTO> responseContent = mapper.paraMembrosResponseDTO(contentListForPage);

        return new PageImpl<>(
                responseContent,
                pageable,
                membrosFiltrados.size()
        );
    }

    private void validarMembrosEncontradosList(List<Membro> membros) {
        if (membros.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum membro encontrado");
        }
    }

    private List<Membro> buscaMembros(String busca) {


        String buscaFormatada = null;
        if (busca != null && !busca.isBlank()) {
            buscaFormatada = "%" + busca + "%";
        }

        List<Membro> membros = repository.findAllWithFilter(buscaFormatada, jwtClaimsExtractor.getIgrejaId());

        if(membros.isEmpty()){
            throw new ObjectNotFoundException("Nenhum membro encontrado");
        }

        return membros;
    }
}
