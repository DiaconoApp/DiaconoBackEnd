package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.membro.MembroResponseDTO;
import com.diacono.diacono.applications.mappers.membro.MembroMapper;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BuscarTodosComFiltroUseCase {

    private final MembroRepository membroRepository;
    private final MembroMapper membroMapper;

    public BuscarTodosComFiltroUseCase(MembroRepository membroRepository, MembroMapper membroMapper) {
        this.membroRepository = membroRepository;
        this.membroMapper = membroMapper;
    }

    @Transactional(readOnly = true)
    public Page<MembroResponseDTO> execute(
            Pageable pageable,
            String termoBusca,
            EnumStatusMembro status,
            UUID fkMinisterio,
            UUID igrejaId
    ) {

        //REFATORAR

        List<Membro> membrosBrutos = buscaMembros(termoBusca, igrejaId);

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
        membrosFiltrados.sort(Comparator.comparing(Membro::getNome, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

        int pageSize = pageable.getPageSize();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageSize), membrosFiltrados.size());

        List<Membro> contentListForPage;

        if (start > end) {
            contentListForPage = Collections.emptyList();
        } else {
            contentListForPage = membrosFiltrados.subList(start, end);
        }

        List<MembroResponseDTO> responseContent = membroMapper.paraMembrosResponseDTO(contentListForPage);

        return new PageImpl<>(
                responseContent,
                pageable,
                membrosFiltrados.size()
        );
    }

    private List<Membro> buscaMembros(String busca, UUID igrejaId) {

        String buscaFormatada = null;
        if (busca != null && !busca.isBlank()) {
            buscaFormatada = "%" + busca + "%";
        }

        List<Membro> membros = membroRepository.findAllWithFilter(buscaFormatada, igrejaId);

        if(membros.isEmpty() || membros == null){
            throw new ObjectNotFoundException("Nenhum membro encontrado");
        }

        return membros;
    }

    private void validarMembrosEncontradosList(List<Membro> membros) {
        if (membros.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum membro encontrado");
        }
    }
}
