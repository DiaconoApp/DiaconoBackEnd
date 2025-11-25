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
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.membro.service.MembroService;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import com.diacono.diacono.ministerio.service.MinisterioService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

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
    private final JwtUtils jwtUtils;


    public EventoService(EventoRepository eventoRepository, EventoMapper eventoMapper, IgrejaService igrejaService, MinisterioService ministerioService, MembroService membroService, EnderecoEventoService enderecoEventoService, EventoUpdateMapper eventoUpdateMapper, RecorrenciaService recorrenciaService, JwtUtils jwtUtils) {
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
        this.igrejaService = igrejaService;
        this.ministerioService = ministerioService;
        this.membroService = membroService;
        this.enderecoEventoService = enderecoEventoService;
        this.eventoUpdateMapper = eventoUpdateMapper;

        this.recorrenciaService = recorrenciaService;
        this.jwtUtils = jwtUtils;
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
        LocalDateTime inicioMes = anoMes.atDay(1).atStartOfDay(); // 1º dia às 00:00
        LocalDateTime fimMes = anoMes.atEndOfMonth().atTime(23, 59, 59);

        List<Evento> eventosAno = eventoRepository.findByPeriodo(inicioMes, fimMes, jwtUtils.getIgrejaId());
        if(eventosAno.isEmpty() || eventosAno == null){
            throw new ObjectNotFoundException("Nenhum evento encontrado para o mês e ano informados");
        }

        EventoSimplificadoDTO eventoResponse = eventoMapper.paraEventoSimplificado(eventosAno);
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

        recorrenciaService.validarRecorrencia(request.recorrencia(), request.dataHoraInicio());
        enderecoEventoService.validarEnderecoEvento(request.endereco());
        validaHoraInicioMenorHoraFim(request.dataHoraInicio(), request.dataHoraFim());
        validarHoraFuturo(request.dataHoraInicio(), request.dataHoraFim());

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

        List<Evento> eventos = eventoRepository.findByPeriodoAndRecorrencia(evento.getRecorrencia(), evento.getDataHoraInicio(), jwtUtils.getIgrejaId());
        System.out.println(eventos);
        if (!eventos.contains(evento)) {
            eventos.add(evento);
        }

        eventoRepository.deleteAll(eventos);

        RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Evento apagado com sucesso");

        return message;

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

        return new RestResponseMessage(HttpStatus.OK, "Evento atualizado com sucesso");

    }

    public EnderecoEventoSimplificadoDTO buscarEnderecoEvento(){
        return enderecoEventoService.buscarEnderecoIgreja();
    }

    //metodos para validar

    private Evento buscarEventoPorUUID(UUID idExterno){

        Evento evento = eventoRepository.findByIdExterno(idExterno);

        if(evento == null){
            throw new ObjectSaveErrorException("Evento não encontrado");
        }

        return evento;
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

    private void validarIdExternoPreenchido(UUID idExterno){
        if(idExterno == null){
            throw new FieldInvalidException("O id do evento precisa ser informado");
        }
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
        evento.setOrganizador(membroService.buscarPorUUID(jwtUtils.getSubject()));
        evento.setIgreja(igrejaService.buscarUUID(jwtUtils.getIgrejaId()));
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

        eventoRepository.saveAll(eventos);

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

        eventoRepository.saveAll(eventos);

        return new RestResponseMessage(HttpStatus.CREATED, "Eventos com recorrência mensal criados com sucesso");
    }


}
