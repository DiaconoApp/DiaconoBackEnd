package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.evento.EventoCreateDTO;
import com.diacono.diacono.applications.mappers.evento.EventoMapper;
import com.diacono.diacono.domain.entity.EnderecoEvento;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.entity.Recorrencia;
import com.diacono.diacono.domain.enums.TipoRecorrencia;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.EnderecoEventoService;
import com.diacono.diacono.usecases.IgrejaService;
import com.diacono.diacono.usecases.MembroService;
import com.diacono.diacono.usecases.MinisterioService;
import com.diacono.diacono.usecases.RecorrenciaService;
import com.diacono.diacono.infrastructure.exceptions.TimeInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class CriarEventoUseCase {

    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;
    private final EnderecoEventoService enderecoEventoService;
    private final RecorrenciaService recorrenciaService;
    private final MinisterioService ministerioService;
    private final MembroService membroService;
    private final IgrejaService igrejaService;
    private final JwtUtils jwtUtils;

    public CriarEventoUseCase(
            EventoRepository eventoRepository,
            EventoMapper eventoMapper,
            EnderecoEventoService enderecoEventoService,
            RecorrenciaService recorrenciaService,
            MinisterioService ministerioService,
            MembroService membroService,
            IgrejaService igrejaService,
            JwtUtils jwtUtils
    ) {
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
        this.enderecoEventoService = enderecoEventoService;
        this.recorrenciaService = recorrenciaService;
        this.ministerioService = ministerioService;
        this.membroService = membroService;
        this.igrejaService = igrejaService;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public RestResponseMessageDTO execute(EventoCreateDTO request) {

        recorrenciaService.validarRecorrencia(request.recorrencia(), request.dataHoraInicio());
        enderecoEventoService.validarEnderecoEvento(request.endereco());
        validaHoraInicioMenorHoraFim(request.dataHoraInicio(), request.dataHoraFim());
        validarHoraFuturo(request.dataHoraInicio(), request.dataHoraFim());

        if (request.recorrencia().tipoRecorrencia().equals(TipoRecorrencia.NAO_REPETE)) {
            criarEventoSemRecorrencia(request);
            return new RestResponseMessageDTO(HttpStatus.CREATED, "Evento sem recorrência criado com sucesso");
        }

        if (request.recorrencia().tipoRecorrencia().equals(TipoRecorrencia.SEMANAL)) {
            criarEventoRecorrenciaSemanal(request);
            return new RestResponseMessageDTO(HttpStatus.CREATED, "Eventos com recorrência semanal criado com sucesso");
        }

        if (request.recorrencia().tipoRecorrencia().equals(TipoRecorrencia.MENSAL)) {
            criarEventoRecorrenciaMensal(request);
            return new RestResponseMessageDTO(HttpStatus.CREATED, "Eventos com recorrência mensal criados com sucesso");
        }

        return new RestResponseMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Motivo não mapeado -> Criação do Evento");
    }


    private Evento criarEventoSemRecorrencia(EventoCreateDTO request) {

        Recorrencia recorrencia = recorrenciaService.converterDtoToRecorrencia(request.recorrencia());

        EnderecoEvento endereco;
        if (request.endereco().idExterno() == null) {
            endereco = enderecoEventoService.converterDtoToEndereco(request.endereco());
        } else {
            endereco = enderecoEventoService.buscarPorUUID(request.endereco().idExterno());
        }

        Evento evento = eventoMapper.paraEvento(request);

        evento.setEnderecoEvento(endereco);
        evento.setRecorrencia(recorrencia);
        evento.setOrganizador(membroService.buscarPorUUID(jwtUtils.getSubject()));
        evento.setIgreja(igrejaService.buscarUUID(jwtUtils.getIgrejaId()));
        evento.setMinisterios(ministerioService.buscarPorUUID(request.fkMinisterios()));

        return eventoRepository.save(evento);
    }


    private RestResponseMessageDTO criarEventoRecorrenciaSemanal(EventoCreateDTO request) {

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

        return new RestResponseMessageDTO(HttpStatus.CREATED, "Eventos com recorrência semanal criado com sucesso");
    }


    private RestResponseMessageDTO criarEventoRecorrenciaMensal(EventoCreateDTO request) {

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

        return new RestResponseMessageDTO(HttpStatus.CREATED, "Eventos com recorrência mensal criados com sucesso");
    }

    private void validaHoraInicioMenorHoraFim(LocalDateTime inicio, LocalDateTime fim) {
        if (fim.isBefore(inicio)) {
            throw new TimeInvalidException("O horário de término do evento precisa ser maior que o horário de início");
        }
    }

    private void validarHoraFuturo(LocalDateTime inicio, LocalDateTime fim) {

        LocalDateTime hojeComMargem = LocalDateTime.now().plusMinutes(1);

        if (inicio.isBefore(hojeComMargem) && fim.isBefore(hojeComMargem)) {
            throw new TimeInvalidException("Não é possível cadastrar eventos com horários passados.");
        }
    }
}