package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioSimplificadoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RevisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase {

    private final EscalaMinisterioRepository escalaMinisterioRepository;
    private final EscalaEventoRepository escalaEventoRepository;
    private final MembroMinisterioRepository membroMinisterioRepository;

    public RevisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase(
            EscalaMinisterioRepository escalaMinisterioRepository,
            EscalaEventoRepository escalaEventoRepository,
            MembroMinisterioRepository membroMinisterioRepository
    ) {
        this.escalaMinisterioRepository = escalaMinisterioRepository;
        this.escalaEventoRepository = escalaEventoRepository;
        this.membroMinisterioRepository = membroMinisterioRepository;
    }

    public List<EscalaMembroMinisterioSimplificadoDTO> execute(
            UUID escalaEventoId,
            UUID igrejaId,
            UUID membroId,
            UUID membroMinisterioIdASerTrocado,
            List<EscalaMembroMinisterioSimplificadoDTO> membrosMinisterioSelecionados
    ) {
        validarEscalaEventoId(escalaEventoId, igrejaId, membroId);

        List<EscalaMembroMinisterioSimplificadoDTO> selecionadosAtuais = Optional
                .ofNullable(membrosMinisterioSelecionados)
                .orElseGet(ArrayList::new);
        int indiceMembroASerTrocado = buscarIndiceMembroASerTrocado(selecionadosAtuais, membroMinisterioIdASerTrocado);

        List<EscalaMembroMinisterioDTO> membrosMinisterio = buscarMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId);
        List<EscalaMembroMinisterioDTO> membrosDisponiveis = filtrarMembrosDisponiveis(igrejaId, escalaEventoId, membrosMinisterio);
        List<EscalaMembroMinisterioDTO> membrosDisponiveisParaRevisao = filtrarMembrosJaSelecionados(
                membrosDisponiveis,
                selecionadosAtuais,
                membroMinisterioIdASerTrocado
        );

        validarHaNovoMembroDisponivel(membrosDisponiveisParaRevisao);
        List<EscalaMembroMinisterioDTO> membrosRandomizados = randomizarMembros(membrosDisponiveisParaRevisao);

        EscalaMembroMinisterioSimplificadoDTO novoMembroSelecionado = membrosRandomizados.stream()
                .limit(1)
                .map(membro -> new EscalaMembroMinisterioSimplificadoDTO(
                        membro.membroMinisterioId(),
                        membro.nomeMembro()
                ))
                .findFirst()
                .orElseThrow(() -> new FieldInvalidException("Nenhum membro disponível para revisão."));

        List<EscalaMembroMinisterioSimplificadoDTO> response = new ArrayList<>(selecionadosAtuais);
        if (indiceMembroASerTrocado >= 0 && indiceMembroASerTrocado < response.size()) {
            response.set(indiceMembroASerTrocado, novoMembroSelecionado);
        } else {
            response.add(novoMembroSelecionado);
        }
        return response;
    }


    private void validarHaNovoMembroDisponivel(List<EscalaMembroMinisterioDTO> membrosDisponiveis) {
        if(membrosDisponiveis.isEmpty()) {
            throw new FieldInvalidException("A quantidade de membros randomizados não pode ser maior que a quantidade de membros disponíveis. Membros disponíveis: 0");
        }
    }

    private void validarEscalaEventoId(UUID escalaEventoId, UUID igrejaId, UUID membroId) {
        UUID ministerioId = escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId);

        if (ministerioId == null) {
            throw new ObjectNotFoundException("Escala evento não encontrada para a igreja informada.");
        }

        List<UUID> listaMinisteriosLider = membroMinisterioRepository
                .buscarMinisterioLider(membroId, igrejaId)
                .stream()
                .map(MinisterioSuperSimplificadoDTO::idExterno)
                .toList();

        if (!listaMinisteriosLider.contains(ministerioId)) {
            throw new ObjectNotFoundException("O líder informado não possui vínculo com o ministério solicitado.");
        }
    }

    private List<EscalaMembroMinisterioDTO> buscarMembroMinisterioByEscalaEventoId(UUID igrejaId, UUID escalaEventoId) {
        return escalaMinisterioRepository
                .findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId);
    }

    private List<EscalaMembroMinisterioDTO> filtrarMembrosDisponiveis(
            UUID igrejaId,
            UUID escalaEventoId,
            List<EscalaMembroMinisterioDTO> membrosMinisterio
    ) {
        Set<UUID> membrosOcupados = Set.copyOf(
                escalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId(igrejaId, escalaEventoId)
        );

        return membrosMinisterio.stream()
                .filter(membro -> !membrosOcupados.contains(membro.membroMinisterioId()))
                .map(membro -> new EscalaMembroMinisterioDTO(
                        membro.membroMinisterioId(),
                        membro.nomeMembro(),
                        membro.status(),
                        false
                ))
                .toList();
    }

    private List<EscalaMembroMinisterioDTO> filtrarMembrosJaSelecionados(
            List<EscalaMembroMinisterioDTO> membrosDisponiveis,
            List<EscalaMembroMinisterioSimplificadoDTO> membrosSelecionados,
            UUID membroMinisterioIdASerTrocado
    ) {
        Set<UUID> idsMembrosSelecionados = membrosSelecionados.stream()
                .map(EscalaMembroMinisterioSimplificadoDTO::idExternoMembroMinisterio)
                .filter(Objects::nonNull)
                .collect(java.util.stream.Collectors.toSet());

        return membrosDisponiveis.stream()
                .filter(membro -> !idsMembrosSelecionados.contains(membro.membroMinisterioId()))
                .filter(membro -> !Objects.equals(membro.membroMinisterioId(), membroMinisterioIdASerTrocado))
                .toList();
    }

    private int buscarIndiceMembroASerTrocado(
            List<EscalaMembroMinisterioSimplificadoDTO> membrosSelecionados,
            UUID membroMinisterioIdASerTrocado
    ) {
        for (int i = 0; i < membrosSelecionados.size(); i++) {
            if (Objects.equals(membrosSelecionados.get(i).idExternoMembroMinisterio(), membroMinisterioIdASerTrocado)) {
                return i;
            }
        }
        throw new FieldInvalidException("O membro a ser trocado não foi encontrado na lista de membros selecionados.");
    }

    private List<EscalaMembroMinisterioDTO> randomizarMembros(List<EscalaMembroMinisterioDTO> membrosDisponiveis) {
        List<EscalaMembroMinisterioDTO> membrosRandomizados = new ArrayList<>(membrosDisponiveis);
        Collections.shuffle(membrosRandomizados);
        return membrosRandomizados;
    }
}
