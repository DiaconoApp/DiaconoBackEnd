package com.diacono.diacono.evento.service;

import com.diacono.diacono.Igreja.service.IgrejaService;
import com.diacono.diacono.evento.model.dto.request.EnderecoEventoDTO;
import com.diacono.diacono.evento.model.dto.request.EventoCreateDTO;
import com.diacono.diacono.evento.model.dto.request.EventoUpdateDTO;
import com.diacono.diacono.evento.model.dto.response.*;
import com.diacono.diacono.evento.model.entity.EnderecoEvento;
import com.diacono.diacono.evento.model.entity.Evento;
import com.diacono.diacono.evento.model.entity.Recorrencia;
import com.diacono.diacono.evento.model.entity.TipoRecorrencia;
import com.diacono.diacono.evento.exceptions.TimeInvalidException;
import com.diacono.diacono.evento.mapper.EventoMapper;
import com.diacono.diacono.evento.repository.EventoRepository;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.membro.service.MembroService;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import com.diacono.diacono.ministerio.service.MinisterioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
// TODO: padronizar validações de Valid em DTOs em requests

@Service
public class EventoService {

    // OWASP A05: logging seguro sem expor dados sensiveis em console.
    private static final Logger logger = LoggerFactory.getLogger(EventoService.class);

    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;
    private final EnderecoEventoService enderecoEventoService;
    private final RecorrenciaService recorrenciaService;
    private final MinisterioService ministerioService;
    private final MembroService membroService;
    private final IgrejaService igrejaService;
    private final JwtUtils jwtUtils;

    public EventoService(EventoRepository eventoRepository,
                        EventoMapper eventoMapper,
                        IgrejaService igrejaService,
                        MinisterioService ministerioService,
                        MembroService membroService,
                        EnderecoEventoService enderecoEventoService,
                        RecorrenciaService recorrenciaService,
                        JwtUtils jwtUtils) {
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
        this.igrejaService = igrejaService;
        this.ministerioService = ministerioService;
        this.membroService = membroService;
        this.enderecoEventoService = enderecoEventoService;
        this.recorrenciaService = recorrenciaService;
        this.jwtUtils = jwtUtils;
    }

    public EventoSimplificadoDTO buscarEventosPorMesEAno(int mes, int ano) {
        if (mes < 1 || mes > 12) {
            throw new FieldInvalidException("O mes precisa estar entre 1 e 12");
        }

        if (ano <= 0) {
            throw new FieldInvalidException("O ano precisa ser maior que 0");
        }

        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDateTime inicioMes = anoMes.atDay(1).atStartOfDay(); // 1º dia às 00:00
        LocalDateTime fimMes = anoMes.atEndOfMonth().atTime(23, 59, 59);

        List<Evento> eventosAno = eventoRepository.findByPeriodo(inicioMes, fimMes, jwtUtils.getIgrejaId());
        if (eventosAno == null || eventosAno.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum evento encontrado para o mes e ano informados");
        }

        return eventoMapper.paraEventoSimplificado(eventosAno);
    }

    public EventoCompletoDTO buscarEventoEspecifico(UUID id) {
        validarIdExternoPreenchido(id);
        Evento evento = buscarEventoPorUUID(id);
        return eventoMapper.paraEventoCompletoDTO(evento);
    }

    @Transactional
    public RestResponseMessage criarEvento(EventoCreateDTO request) {
        // OWASP A01/A07: validacao defensiva de payload obrigatorio.
        if (request == null) {
            throw new ObjectSaveErrorException("Dados do evento nao podem ser nulos");
        }
        if (request.fkMinisterios() == null || request.fkMinisterios().isEmpty()) {
            throw new FieldInvalidException("O evento deve possuir pelo menos um ministerio");
        }

        recorrenciaService.validarRecorrencia(request.recorrencia(), request.dataHoraInicio());
        enderecoEventoService.validarEnderecoEvento(request.endereco());
        validaHoraInicioMenorHoraFim(request.dataHoraInicio(), request.dataHoraFim());
        validarHoraFuturo(request.dataHoraInicio(), request.dataHoraFim());

        if(request.recorrencia().tipoRecorrencia().equals(TipoRecorrencia.NAO_REPETE)) {
            criarEventoSemRecorrencia(request);
            return new RestResponseMessage(HttpStatus.CREATED, "Evento sem recorrência criado com sucesso");
        }

        if(request.recorrencia().tipoRecorrencia().equals(TipoRecorrencia.SEMANAL)) {
            return criarEventoRecorrenciaSemanal(request);
        }

        if(request.recorrencia().tipoRecorrencia().equals(TipoRecorrencia.MENSAL)) {
            return criarEventoRecorrenciaMensal(request);
        }

        return new RestResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Motivo não mapeado -> Criação do Evento");
    }

    @Transactional
    public RestResponseMessage apagarEvento(UUID idExterno){
        validarIdExternoPreenchido(idExterno);

        long deleteCount = eventoRepository.deleteByIdExterno(idExterno);

        if(deleteCount == 0){
            throw new ObjectNotFoundException("Não foi possível apagar o evento, verifique se o evento existe");
        }

        return new RestResponseMessage(HttpStatus.OK, "Evento apagado com sucesso");
    }

    @Transactional
    public RestResponseMessage apagarEventosMultiplos(UUID idEvento){
        validarIdExternoPreenchido(idEvento);

        Evento evento = buscarEventoPorUUID(idEvento);

        List<Evento> eventos = eventoRepository.findByPeriodoAndRecorrencia(
                evento.getRecorrencia(),
                evento.getDataHoraInicio(),
                jwtUtils.getIgrejaId()
        );

        if (!eventos.contains(evento)) {
            eventos.add(evento);
        }

        eventoRepository.deleteAll(eventos);
        logger.debug("Eventos de recorrencia removidos para evento base informado");

        return new RestResponseMessage(HttpStatus.OK, "Evento apagado com sucesso");
    }

    @Transactional
    public RestResponseMessage alterarEvento(EventoUpdateDTO request, UUID idExterno) {
        validarIdExternoPreenchido(idExterno);
        Evento evento = buscarEventoPorUUID(idExterno);

        //validar endereço e ver diferenças -> para atualizar apenas se houver mudanças
        if(request.endereco() != null && validarEnderecoDiferente(evento.getEnderecoEvento(), request.endereco())){
            EnderecoEvento enderecoAtualizado;
            if (request.endereco().idExterno() == null) {
                enderecoAtualizado = enderecoEventoService.converterDtoToEndereco(request.endereco());
            } else {
                enderecoAtualizado = enderecoEventoService.buscarPorUUID(request.endereco().idExterno());
            }
            evento.setEnderecoEvento(enderecoAtualizado);
        }

        if (request.fkMinisterios() != null && !request.fkMinisterios().isEmpty()) {
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

        LocalDateTime novoInicio = request.dataHoraInicio() != null ? request.dataHoraInicio() : evento.getDataHoraInicio();
        LocalDateTime novoFim = request.dataHoraFim() != null ? request.dataHoraFim() : evento.getDataHoraFim();
        // OWASP A07: garante consistencia temporal em atualizacao parcial.
        validaHoraInicioMenorHoraFim(novoInicio, novoFim);

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

    public EnderecoEventoSimplificadoDTO buscarEnderecoEvento() {
        return enderecoEventoService.buscarEnderecoIgreja();
    }

    private Evento buscarEventoPorUUID(UUID idExterno) {
        Evento evento = eventoRepository.findByIdExterno(idExterno);

        if (evento == null) {
            throw new ObjectNotFoundException("Evento nao encontrado");
        }

        return evento;
    }

    private void validaHoraInicioMenorHoraFim(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new TimeInvalidException("Horario de inicio e fim sao obrigatorios");
        }
        if (!fim.isAfter(inicio)) {
            throw new TimeInvalidException("O horario de termino do evento precisa ser maior que o horario de inicio");
        }
    }

    private void validarHoraFuturo(LocalDateTime inicio, LocalDateTime fim) {
        LocalDateTime hojeComMargem = LocalDateTime.now().plusMinutes(1);
        if (inicio.isBefore(hojeComMargem) || fim.isBefore(hojeComMargem)) {
            throw new TimeInvalidException("Nao e possivel cadastrar eventos com horarios passados.");
        }
    }

    private void validarIdExternoPreenchido(UUID idExterno) {
        if (idExterno == null) {
            throw new FieldInvalidException("O id do evento precisa ser informado");
        }
    }

    private boolean validarEnderecoDiferente(EnderecoEvento endereco, EnderecoEventoDTO enderecoEventoDTO) {
        if (endereco == null || enderecoEventoDTO == null) {
            return false;
        }

        if (enderecoEventoDTO.idExterno() == null) {
            if (enderecoEventoDTO.cep() == null) {
                return false;
            }

            return !enderecoEventoDTO.cep().equals(endereco.getCep())
                    || enderecoEventoDTO.numero() == null
                    || !enderecoEventoDTO.numero().equals(endereco.getNumero());
        }

        return !enderecoEventoDTO.idExterno().equals(endereco.getIdExterno());
    }

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

        return eventoRepository.save(evento);
    }

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
        return new RestResponseMessage(HttpStatus.CREATED, "Eventos com recorrencia semanal criado com sucesso");
    }

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
        return new RestResponseMessage(HttpStatus.CREATED, "Eventos com recorrencia mensal criados com sucesso");
    }

    // Utilizado para escalas
//    public List<EventoComEventoMinisterioDTO> buscarEventosComEventoMinisterioPorMesAno(int mes, int ano) {
//
//        if(mes < 1 || mes > 12){
//            throw new FieldInvalidException("O mês precisa estar entre 1 e 12");
//        }
//
//        if(ano <= 0) {
//            throw new FieldInvalidException("O ano precisa ser maior que 0");
//        }
//
//        List<EventoComEventoMinisterioDTO> eventosEventoMinisterio =  eventoRepository.findEventosComEventoMinisterioPorMesAno(mes, ano, jwtUtils.getIgrejaId());
//
//        if (eventosEventoMinisterio.isEmpty()) {
//            throw new ObjectNotFoundException("Nenhum evento encontrado para o mês e ano informados");
//        }
//
//        return eventosEventoMinisterio;
//    }

    //UTILIZADO PARA KPI

    public List<EventoKpiDTO> buscarKpisEvento(int anoInicio, int anoFim) {
        UUID idIgreja = jwtUtils.getIgrejaId();
        return eventoRepository.buscarKpisEvento(anoInicio, anoFim, idIgreja);
    }

    public List<MinisterioEventoDashDTO> ministerioBuscarDashQuantidadeEventos(int anoInicio, int anoFim) {
        UUID idIgreja = jwtUtils.getIgrejaId();
        List<MinisterioEventoDashDTO> response = eventoRepository.contarEventosPorMinisterioNoPeriodo(anoInicio, anoFim, idIgreja);

        // OWASP A05: remove saida de debug em console.
        logger.debug("Dashboard de quantidade de eventos por ministerio calculado para a igreja autenticada");
        return response;

    }
}
