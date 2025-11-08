package com.diacono.diacono.evento.service;

import com.diacono.diacono.Igreja.service.IgrejaService;
import com.diacono.diacono.evento.mapper.EnderecoEventoMapper;
import com.diacono.diacono.evento.mapper.RecorrenciaMapper;
import com.diacono.diacono.evento.model.dto.request.EnderecoEventoDTO;
import com.diacono.diacono.evento.model.dto.request.RecorrenciaCreateDTO;
import com.diacono.diacono.evento.model.dto.response.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.evento.model.entity.EnderecoEvento;
import com.diacono.diacono.evento.model.entity.Recorrencia;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.evento.exceptions.TimeInvalidException;
import com.diacono.diacono.evento.mapper.EventoMapper;
import com.diacono.diacono.evento.mapper.EventoUpdateMapper;
import com.diacono.diacono.evento.model.dto.request.EventoCreateDTO;
import com.diacono.diacono.evento.model.dto.request.EventoUpdateDTO;
import com.diacono.diacono.evento.model.dto.response.EventoCompletoDTO;
import com.diacono.diacono.evento.model.dto.response.EventoSimplificadoDTO;
import com.diacono.diacono.evento.model.entity.Evento;
import com.diacono.diacono.evento.model.entity.TipoRecorrencia;
import com.diacono.diacono.evento.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.membro.service.MembroService;
import com.diacono.diacono.ministerio.service.MinisterioService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;
    private final EventoUpdateMapper eventoUpdateMapper;
    private final EnderecoEventoService enderecoEventoService;
    private final RecorrenciaService recorrenciaService;
    private final MinisterioService ministerioService;
    private final MembroService membroService;
    private final IgrejaService igrejaService;

    public EventoService(EventoRepository eventoRepository, EventoMapper eventoMapper, IgrejaService igrejaService, MinisterioService ministerioService, MembroService membroService, EnderecoEventoService enderecoEventoService, EventoUpdateMapper eventoUpdateMapper, RecorrenciaService recorrenciaService) {
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
        this.igrejaService = igrejaService;
        this.ministerioService = ministerioService;
        this.membroService = membroService;
        this.enderecoEventoService = enderecoEventoService;
        this.eventoUpdateMapper = eventoUpdateMapper;

        this.recorrenciaService = recorrenciaService;
    }

    public EventoSimplificadoDTO buscarEventosPorMesEAno(int mes, int ano){

        //completo

        if(mes < 1 || mes > 12){
            throw new FieldInvalidException("O mês precisa estar entre 1 e 12");
        }

        if(ano <= 0) {
            throw new FieldInvalidException("O ano precisa ser maior que 0");
        }

        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDate inicioMes = anoMes.atDay(1);
        LocalDate fimMes = anoMes.atEndOfMonth();

        List<Evento> todosOsEventosMes = new ArrayList<>();
        List<Evento> eventosAno = eventoRepository.findByPeriodo(inicioMes, fimMes);

        if(eventosAno.isEmpty() || eventosAno == null){
            throw new ObjectNotFoundException("Nenhum evento encontrado para o mês e ano informados");
        }

        EventoSimplificadoDTO eventoResponse = eventoMapper.paraEventoSimplificado(todosOsEventosMes);
        return eventoResponse;
    }

    public EventoCompletoDTO buscarEventoEspecifico(UUID id){

        //completo
        validarIdExternoPreenchido(id);
        Evento evento = eventoRepository.findByIdExterno(id);
        EventoCompletoDTO eventoResponse = eventoMapper.paraEventoCompletoDTO(evento);

        return eventoResponse;
    }

    @Transactional
    public RestResponseMessage criarEvento(EventoCreateDTO request){

       //CONCLUÍDO

        //VALIDAR PRIMEIROS TODOS OS CAMPOS PREENCHIDOS

        recorrenciaService.validarRecorrencia(request.recorrencia());
        enderecoEventoService.validarEnderecoEvento(request.endereco());
        validaHoraInicioMenorHoraFim(request.horaInicio(), request.horaFim());
        validarHoraFuturo(request.data(),request.horaInicio(), request.horaFim());

        //CRIAR EVENTO SEM RECORRENCIA

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
    public RestResponseMessage apagarEvento(UUID idExterno){

        //completo

        validarIdExternoPreenchido(idExterno);

        long deleteCount = eventoRepository.deleteByIdExterno(idExterno);

        if(deleteCount == 0){
            throw new ObjectNotFoundException("Não foi possível apagar o evento, verifique se o evento existe");
        }

        RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Evento apagado com sucesso");

        return message;

    }

    @Transactional
    public RestResponseMessage apagarEventosMultiplos(UUID idEvento){

        validarIdExternoPreenchido(idEvento);

        Evento evento = eventoRepository.findByIdExterno(idEvento);
        if (evento == null) {
            throw new ObjectNotFoundException("Não foi possível apagar o evento, verifique se o evento existe");
        }

        List<Evento> eventos = eventoRepository.findByPeriodoAndRecorrencia(evento.getRecorrencia(), evento.getData());

        if (!eventos.contains(evento)) {
            eventos.add(evento);
        }

        eventoRepository.deleteAll(eventos);

        RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Evento apagado com sucesso");

        return message;

    }

  /*  @Transactional
    public RestResponseMessage alterarEvento(EventoUpdateDTO request, UUID idExterno){

        //completo

        validarIdExternoPreenchido(idExterno);
        Evento evento = eventoRepository.findByIdExterno(idExterno);
        validarEventoExistente(evento);

        if(request.fkMinisterios() != null && !request.fkMinisterios().isEmpty()){
            evento.setMinisterios(ministerioService.buscarPorUUID(request.fkMinisterios()));
        }
        if(request.endereco().idExterno() != null){
            evento.setEnderecoEvento(enderecoEventoService.buscarPorUUID(request.endereco().idExterno()));
        }

        eventoUpdateMapper.updateEventoDTO(request, evento);

        if(request.horaInicio() != null && request.horaFim() != null){
            validaHoraInicioMenorHoraFim(request.horaInicio(), request.horaFim());
            validarHoraFuturo(evento.getData(), request.horaInicio(), request.horaFim());
        } else if(request.horaInicio() != null){
            validaHoraInicioMenorHoraFim(request.horaInicio(), evento.getHoraFim());
            validarHoraFuturo(evento.getData(), request.horaInicio(), evento.getHoraFim());
        } else if(request.horaFim() != null){
            validaHoraInicioMenorHoraFim(evento.getHoraInicio(), request.horaFim());
            validarHoraFuturo(evento.getData(), evento.getHoraInicio(), request.horaFim());
        }

        Evento eventoAtualizado = eventoRepository.save(evento);

        if(eventoAtualizado == null){
            throw new ObjectSaveErrorException("Não foi possível atualizar o evento");
        }

        return new RestResponseMessage(HttpStatus.OK, "Evento atualizado com sucesso");
    }*/

    public EnderecoEventoSimplificadoDTO buscarEnderecoEvento(){
        return enderecoEventoService.buscarEnderecoIgreja();
    }

    //metodos para validar

    private void validarEventoExistente(Evento evento){
        if(evento == null){
            throw new ObjectSaveErrorException("Evento não encontrado");
        }
    }

    private void validaHoraInicioMenorHoraFim(LocalTime inicio, LocalTime fim){
        if(fim.isBefore(inicio)){
            throw new TimeInvalidException("O horário de término do evento precisa ser maior que o horário de início");
        }
    }

    private void validarHoraFuturo(LocalDate data,LocalTime inicio, LocalTime fim){
        LocalDate hoje = LocalDate.now();
        LocalTime agora = LocalTime.now();
        LocalTime agoraComMargem = agora.minusMinutes(3);
        if(hoje.isEqual(data) && (inicio.isBefore(agoraComMargem) || fim.isBefore(agoraComMargem))){
            throw new TimeInvalidException("Não é possível cadastrar eventos com horários passados.");
        }
    }

    private void validarIdExternoPreenchido(UUID idExterno){
        if(idExterno == null){
            throw new FieldInvalidException("O id do evento precisa ser informado");
        }
    }

// metodos para auxiliar

    @Transactional
    private Evento criarEventoSemRecorrencia(EventoCreateDTO request){

        //CRIAR RECORRENCIA

        Recorrencia recorrencia = recorrenciaService.converterDtoToRecorrencia(request.recorrencia());

        //CRIAR ENDERECO

        EnderecoEvento endereco;
        if (request.endereco().idExterno() == null) {
            endereco = enderecoEventoService.converterDtoToEndereco(request.endereco());
        } else {
            endereco = enderecoEventoService.buscarPorUUID(request.endereco().idExterno());
        }

        //CRIAR EVENTO

        Evento evento = eventoMapper.paraEvento(request);

        evento.setEnderecoEvento(endereco);
        evento.setRecorrencia(recorrencia);
        evento.setOrganizador(membroService.buscarPorUUID(request.fkOrganizador()));
        evento.setIgreja(igrejaService.buscarUUID(request.fkIgreja()));
        evento.setMinisterios(ministerioService.buscarPorUUID(request.fkMinisterios()));

        Evento eventoSalvo = eventoRepository.save(evento);

        return eventoSalvo;

    }

    @Transactional
    private RestResponseMessage criarEventoRecorrenciaSemanal(EventoCreateDTO request){

        //CRIAR EVENTO

        Evento evento = criarEventoSemRecorrencia(request);

        List<Evento> eventos = new ArrayList<>();

        LocalDate dataInicio = request.recorrencia().dataInicioRecorrencia();
        LocalDate dataFim = request.recorrencia().dataTerminoRecorrencia();

        long semanas = ChronoUnit.WEEKS.between(dataInicio, dataFim);

        for (int i = 1; i <= semanas; i++) {
            Evento novoEvento = new Evento();

            novoEvento.setIgreja(evento.getIgreja());
            novoEvento.setOrganizador(evento.getOrganizador());
            novoEvento.setEnderecoEvento(evento.getEnderecoEvento());
            novoEvento.setMinisterios(evento.getMinisterios());
            novoEvento.setRecorrencia(evento.getRecorrencia());
            novoEvento.setNome(evento.getNome());
            novoEvento.setDescricao(evento.getDescricao());
            novoEvento.setPublicoAlvo(evento.getPublicoAlvo());
            novoEvento.setData(dataInicio.plusWeeks(i));
            novoEvento.setHoraInicio(evento.getHoraInicio());
            novoEvento.setHoraFim(evento.getHoraFim());
            novoEvento.setCusto(evento.getCusto());
            eventos.add(novoEvento);
        }

        eventoRepository.saveAll(eventos);

        return new RestResponseMessage(HttpStatus.CREATED, "Evento sem recorrência criado com sucesso");

    }

    @Transactional
    private RestResponseMessage criarEventoRecorrenciaMensal(EventoCreateDTO request) {

        //CRIAR EVENTO

        Evento eventoBase = criarEventoSemRecorrencia(request);

        List<Evento> eventos = new ArrayList<>();

        LocalDate dataInicio = request.recorrencia().dataInicioRecorrencia();
        LocalDate dataFim = request.recorrencia().dataTerminoRecorrencia();

        long meses = ChronoUnit.MONTHS.between(dataInicio, dataFim);

        for (int i = 1; i <= meses; i++) {
            Evento novoEvento = new Evento();
            novoEvento.setIgreja(eventoBase.getIgreja());
            novoEvento.setOrganizador(eventoBase.getOrganizador());
            novoEvento.setEnderecoEvento(eventoBase.getEnderecoEvento());
            novoEvento.setMinisterios(eventoBase.getMinisterios());
            novoEvento.setRecorrencia(eventoBase.getRecorrencia());
            novoEvento.setNome(eventoBase.getNome());
            novoEvento.setDescricao(eventoBase.getDescricao());
            novoEvento.setPublicoAlvo(eventoBase.getPublicoAlvo());
            novoEvento.setData(dataInicio.plusMonths(i));
            novoEvento.setHoraInicio(eventoBase.getHoraInicio());
            novoEvento.setHoraFim(eventoBase.getHoraFim());
            novoEvento.setCusto(eventoBase.getCusto());
            eventos.add(novoEvento);
        }

        eventoRepository.saveAll(eventos);

        return new RestResponseMessage(HttpStatus.CREATED, "Eventos mensais criados com sucesso");
    }


}
