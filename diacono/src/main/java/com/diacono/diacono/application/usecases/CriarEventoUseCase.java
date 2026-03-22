package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.exceptions.TimeInvalidException;
import com.diacono.diacono.application.mappers.EventoMapper;
import com.diacono.diacono.domain.entities.EnderecoEvento;
import com.diacono.diacono.domain.entities.Evento;
import com.diacono.diacono.domain.entities.Recorrencia;
import com.diacono.diacono.domain.enums.TipoRecorrencia;
import com.diacono.diacono.application.exceptions.FieldInvalidException;
import com.diacono.diacono.infrastructure.persistence.EventoRepository;
import com.diacono.diacono.presentation.dto.request.EnderecoEventoDTO;
import com.diacono.diacono.presentation.dto.request.EventoCreateDTO;
import com.diacono.diacono.presentation.dto.request.RecorrenciaCreateDTO;
import com.diacono.diacono.presentation.dto.response.RestResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class CriarEventoUseCase {

    private final EventoRepository repository;
    private final EventoMapper eventoMapper;
    

    public CriarEventoUseCase(EventoRepository repository, EventoMapper eventoMapper) {
        this.repository = repository;
        this.eventoMapper = eventoMapper;
    }

    @Transactional
    public RestResponseMessage criarEvento(EventoCreateDTO request){
        validarRecorrencia(request.recorrencia(), request.dataHoraInicio());
        validarEnderecoEvento(request.endereco());
        validaHoraInicioMenorHoraFim(request.dataHoraInicio(), request.dataHoraFim());
        validarHoraFuturo(request.dataHoraInicio(), request.dataHoraFim());

        if(request.recorrencia().tipoRecorrencia().equals(TipoRecorrencia.NAO_REPETE)){
            criarEventoSemRecorrencia(request);
            return new RestResponseMessage(HttpStatus.CREATED, "Evento sem recorrência criado com sucesso");
        }

        if(request.recorrencia().tipoRecorrencia().equals(TipoRecorrencia.SEMANAL)){
            return criarEventoRecorrenciaSemanal(request);
        }

        if(request.recorrencia().tipoRecorrencia().equals(TipoRecorrencia.MENSAL)){
            return criarEventoRecorrenciaMensal(request);
        }

        return new RestResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Motivo não mapeado -> Criação do Evento");
    }

    @Transactional
    private RestResponseMessage criarEventoRecorrenciaSemanal(EventoCreateDTO request){

        //CRIAR EVENTO

        Evento evento = criarEventoSemRecorrencia(request);

        List<Evento> eventos = new ArrayList<>();

        LocalDate dataInicio = request.recorrencia().dataInicioRecorrencia();
        LocalDate dataFim = request.recorrencia().dataTerminoRecorrencia();
        LocalTime horarioInicio = request.dataHoraInicio().toLocalTime();
        LocalTime horarioFim = request.dataHoraFim().toLocalTime();
        long semanas = ChronoUnit.WEEKS.between(dataInicio, dataFim);

        for (int i = 1; i <= semanas; i++) {
            Evento novoEvento = new Evento();

            novoEvento.setIgreja(evento.getIgreja());
            novoEvento.setOrganizador(evento.getOrganizador());
            novoEvento.setEnderecoEvento(evento.getEnderecoEvento());
            novoEvento.setMinisterios(new HashSet<>(evento.getMinisterios()));
            novoEvento.setRecorrencia(evento.getRecorrencia());
            novoEvento.setNome(evento.getNome());
            novoEvento.setDescricao(evento.getDescricao());
            novoEvento.setPublicoAlvo(evento.getPublicoAlvo());
            novoEvento.setDataHoraInicio(evento.getDataHoraInicio().plusWeeks(i));
            novoEvento.setDataHoraFim(evento.getDataHoraFim().plusWeeks(i));
            novoEvento.setCusto(evento.getCusto());
            eventos.add(novoEvento);
        }

        repository.saveAll(eventos);

        return new RestResponseMessage(HttpStatus.CREATED, "Eventos com recorrência semanal criado com sucesso");

    }

    @Transactional
    private RestResponseMessage criarEventoRecorrenciaMensal(EventoCreateDTO request) {

        //CRIAR EVENTO

        Evento eventoBase = criarEventoSemRecorrencia(request);

        List<Evento> eventos = new ArrayList<>();

        LocalDate dataInicio = request.recorrencia().dataInicioRecorrencia();
        LocalDate dataFim = request.recorrencia().dataTerminoRecorrencia();
        LocalTime horarioInicio = request.dataHoraInicio().toLocalTime();
        LocalTime horarioFim = request.dataHoraFim().toLocalTime();
        long meses = ChronoUnit.MONTHS.between(dataInicio, dataFim);

        for (int i = 1; i <= meses; i++) {
            Evento novoEvento = new Evento();
            novoEvento.setIgreja(eventoBase.getIgreja());
            novoEvento.setOrganizador(eventoBase.getOrganizador());
            novoEvento.setEnderecoEvento(eventoBase.getEnderecoEvento());
            novoEvento.setMinisterios(new HashSet<>(eventoBase.getMinisterios()));
            novoEvento.setRecorrencia(eventoBase.getRecorrencia());
            novoEvento.setNome(eventoBase.getNome());
            novoEvento.setDescricao(eventoBase.getDescricao());
            novoEvento.setPublicoAlvo(eventoBase.getPublicoAlvo());
            novoEvento.setDataHoraInicio(eventoBase.getDataHoraInicio().plusMonths(i));
            novoEvento.setDataHoraFim(eventoBase.getDataHoraFim().plusMonths(i));
            novoEvento.setCusto(eventoBase.getCusto());
            eventos.add(novoEvento);
        }

        repository.saveAll(eventos);

        return new RestResponseMessage(HttpStatus.CREATED, "Eventos com recorrência mensal criados com sucesso");
    }

    private void validarRecorrencia(RecorrenciaCreateDTO recorrencia, LocalDateTime comparativoEvento){

        if(!recorrencia.tipoRecorrencia().equals(TipoRecorrencia.NAO_REPETE)){

            if(recorrencia.dataTerminoRecorrencia() == null || recorrencia.dataInicioRecorrencia() == null){
                throw  new FieldInvalidException("É necessário preencher os campos de inicío e término da recorrência");
            }

            if(!recorrencia.dataTerminoRecorrencia().isAfter(recorrencia.dataInicioRecorrencia())){
                throw new FieldInvalidException("A data final da recorrência precisa ser maior que a data de início, para eventos com recorrência.");
            }

            if(recorrencia.dataInicioRecorrencia().plusDays(365).isBefore(recorrencia.dataTerminoRecorrencia())){
                throw  new FieldInvalidException("A data final da recorrência precisar estar dentro do período de um ano");
            }

            if(!recorrencia.dataInicioRecorrencia().isEqual(comparativoEvento.toLocalDate())){
                throw new FieldInvalidException("A data de início da recorrência precisa ser igual à data de início do evento.");
            }

        }
    }

    private void validaHoraInicioMenorHoraFim(LocalDateTime inicio, LocalDateTime fim){
        if(fim.isBefore(inicio)){
            throw new TimeInvalidException("O horário de término do evento precisa ser maior que o horário de início");
        }
    }

    private void validarHoraFuturo(LocalDateTime inicio, LocalDateTime fim){

        LocalDateTime hojeDataHora = LocalDateTime.now();
        LocalDateTime hojeComMargem = hojeDataHora.plusMinutes(1);
        if(inicio.isBefore(hojeComMargem) && fim.isBefore(hojeComMargem)){
            throw new TimeInvalidException("Não é possível cadastrar eventos com horários passados.");
        }
    }

    private void validarEnderecoEvento(EnderecoEventoDTO endereco){

        if (endereco.idExterno() != null) {
            return;
        }

        if(endereco.idExterno() == null){
            if(endereco.cep() == null || endereco.cep().isBlank()){
                throw  new FieldInvalidException("Você precisa preencher o CEP");
            }
        }

        if (endereco.cep() == null || endereco.cep().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o CEP");
        }

        if (endereco.estado() == null  || endereco.estado().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o estado");
        }

        if (endereco.cidade() == null  ||  endereco.cidade().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher a cidade");
        }

        if (endereco.bairro() == null  ||  endereco.bairro().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o bairro");
        }

        if (endereco.rua() == null  || endereco.rua().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher a rua");
        }

        if (endereco.numero() == null  || endereco.numero().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o número");
        }

        if (endereco.apelido() == null  ||  endereco.apelido().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o apelido");
        }

    }

    @Transactional
    private Evento criarEventoSemRecorrencia(EventoCreateDTO request){

        //CRIAR RECORRENCIA

        Recorrencia recorrencia = recorrenciaMapper.paraRecorrencia(request.recorrencia());

        //CRIAR ENDERECO

        EnderecoEvento endereco;
        if (request.endereco().idExterno() == null) {
            endereco = enderecoEventoMapper.paraEndereco(enderecoEventoDTO);
        } else {
            endereco = enderecoEventoService.buscarPorUUID(request.endereco().idExterno());
        }

        //CRIAR EVENTO

        Evento evento = eventoMapper.paraEvento(request);

        evento.setEnderecoEvento(endereco);
        evento.setRecorrencia(recorrencia);
        evento.setOrganizador(membroService.buscarPorUUID(jwtClaimsExtractor.getSubject()));
        evento.setIgreja(igrejaService.buscarUUID(jwtClaimsExtractor.getIgrejaId()));
        evento.setMinisterios(ministerioService.buscarPorUUID(request.fkMinisterios()));

        Evento eventoSalvo = repository.save(evento);

        return eventoSalvo;

    }
}
