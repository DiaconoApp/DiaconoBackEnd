package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.service.EnderecoEventoFetcher;
import com.diacono.diacono.application.service.MinisteriosFetcher;
import com.diacono.diacono.domain.entities.EnderecoEvento;
import com.diacono.diacono.domain.entities.Evento;
import com.diacono.diacono.domain.entities.Ministerio;
import com.diacono.diacono.application.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.infrastructure.persistence.EventoRepository;
import com.diacono.diacono.presentation.dto.request.EnderecoEventoDTO;
import com.diacono.diacono.presentation.dto.request.EventoUpdateDTO;
import com.diacono.diacono.presentation.dto.response.RestResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.diacono.diacono.application.validators.EventoValidator.validarIdExternoPreenchido;

@Service
public class AlterarEventoUseCase {

    private final EventoRepository repository;
    private final MinisteriosFetcher ministeriosFetcher;
    private final EnderecoEventoFetcher enderecoEventoFetcher;

    public AlterarEventoUseCase(EventoRepository repository, MinisteriosFetcher ministeriosFetcher, EnderecoEventoFetcher enderecoEventoFetcher) {
        this.repository = repository;
        this.ministeriosFetcher = ministeriosFetcher;
        this.enderecoEventoFetcher = enderecoEventoFetcher;
    }

    @Transactional
    public RestResponseMessage alterarEvento(EventoUpdateDTO request, UUID idExterno){

        //completo

        validarIdExternoPreenchido(idExterno);
        Evento evento = buscarEventoPorUUID(idExterno);

        //validar endereço e ver diferenças -> para atualizar apenas se houver mudanças

        if(request.endereco() != null && validarEnderecoDiferente(evento.getEnderecoEvento(), request.endereco())){
            EnderecoEvento enderecoAtualizado;
            if(request.endereco().idExterno() == null){
                enderecoAtualizado = enderecoEventoFetcher.converterDtoToEndereco(request.endereco());
            } else {
                enderecoAtualizado = enderecoEventoFetcher.buscarPorUUID(request.endereco().idExterno());
            }
            evento.setEnderecoEvento(enderecoAtualizado);
        }

        //validar outros campos que precisam ser atualizados

        if(request.fkMinisterios() != null && !request.fkMinisterios().isEmpty()){
            Set<Ministerio> ministerios = new HashSet<>(ministeriosFetcher.buscarPorUUID(request.fkMinisterios()));
            evento.getMinisterios().clear();
            evento.getMinisterios().addAll(ministerios);
        }

        if (request.nome() != null) {
            evento.setNome(request.nome());
        }

        if (request.descricao() != null) {
            evento.setDescricao(request.descricao());
        }

        if (request.publicoAlvo() != null) {
            evento.setPublicoAlvo(request.publicoAlvo());
        }

        if (request.dataHoraInicio() != null) {
            evento.setDataHoraInicio(request.dataHoraInicio());
        }

        if (request.dataHoraFim() != null) {
            evento.setDataHoraFim(request.dataHoraFim());
        }

        if (request.custo() != null) {
            evento.setCusto(request.custo());
        }

        repository.save(evento);

        return new RestResponseMessage(HttpStatus.OK, "Evento atualizado com sucesso");

    }

    private Evento buscarEventoPorUUID(UUID idExterno){

        Evento evento = repository.findByIdExterno(idExterno);

        if(evento == null){
            throw new ObjectSaveErrorException("Evento não encontrado");
        }

        return evento;
    }

    private boolean validarEnderecoDiferente(EnderecoEvento endereco, EnderecoEventoDTO enderecoEventoDTO){

        if(enderecoEventoDTO.idExterno() == null){

            if(enderecoEventoDTO.cep() == null){
                return false;
            }

            return !enderecoEventoDTO.cep().equals(endereco.getCep()) || enderecoEventoDTO.numero() == null || !enderecoEventoDTO.numero().equals(endereco.getNumero());
        }

        return !enderecoEventoDTO.idExterno().equals(endereco.getIdExterno());
    }

}
