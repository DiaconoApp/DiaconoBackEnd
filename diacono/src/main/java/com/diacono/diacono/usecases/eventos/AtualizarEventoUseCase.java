package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.evento.EnderecoEventoDTO;
import com.diacono.diacono.applications.dtos.evento.EventoUpdateDTO;
import com.diacono.diacono.applications.mappers.evento.EventoUpdateMapper;
import com.diacono.diacono.domain.entity.EnderecoEvento;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.exceptions.TimeInvalidException;
import com.diacono.diacono.usecases.EnderecoEventoService;
import com.diacono.diacono.usecases.MinisterioService;
import com.diacono.diacono.usecases.eventos.validation.ValidarIdExternoPreenchido;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class AtualizarEventoUseCase {

    private final EventoRepository eventoRepository;
    private final EnderecoEventoService enderecoEventoService;
    private final MinisterioService ministerioService;
    private final ValidarIdExternoPreenchido validarIdExternoPreenchido;

    public AtualizarEventoUseCase(EventoRepository eventoRepository, EnderecoEventoService enderecoEventoService, MinisterioService ministerioService, ValidarIdExternoPreenchido validarIdExternoPreenchido) {
        this.eventoRepository = eventoRepository;
        this.enderecoEventoService = enderecoEventoService;
        this.ministerioService = ministerioService;
        this.validarIdExternoPreenchido = validarIdExternoPreenchido;
    }

    @Transactional
    public RestResponseMessageDTO execute(EventoUpdateDTO request, UUID idExterno){

        validarIdExternoPreenchido.validarIdExternoPreenchido(idExterno);
        Evento evento = buscarEventoPorUUID(idExterno);

        //validar endereço e ver diferenças -> para atualizar apenas se houver mudanças

        if(request.endereco() != null && validarEnderecoDiferente(evento.getEnderecoEvento(), request.endereco())){
            EnderecoEvento enderecoAtualizado;
            if(request.endereco().idExterno() == null){
                enderecoAtualizado = enderecoEventoService.converterDtoToEndereco(request.endereco());
            } else {
                enderecoAtualizado = enderecoEventoService.buscarPorUUID(request.endereco().idExterno());
            }
            evento.setEnderecoEvento(enderecoAtualizado);
        }

        //validar outros campos que precisam ser atualizados

        if(request.fkMinisterios() != null && !request.fkMinisterios().isEmpty()){
            Set<Ministerio> ministerios = new HashSet<>(ministerioService.buscarPorUUID(request.fkMinisterios()));
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

        eventoRepository.save(evento);

        return new RestResponseMessageDTO(HttpStatus.OK, "Evento atualizado com sucesso");

    }

    //metodos para validar

    private Evento buscarEventoPorUUID(UUID idExterno){

        Evento evento = eventoRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ObjectNotFoundException("Evento não encontrado"));;

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