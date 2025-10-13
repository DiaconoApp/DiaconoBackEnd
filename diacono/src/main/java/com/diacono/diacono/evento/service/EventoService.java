package com.diacono.diacono.evento.service;

import com.diacono.diacono.Igreja.service.IgrejaService;
import com.diacono.diacono.endereco.mapper.EnderecoEventoMapper;
import com.diacono.diacono.endereco.model.dto.request.EnderecoEventoDTO;
import com.diacono.diacono.endereco.model.entity.EnderecoEvento;
import com.diacono.diacono.endereco.service.EnderecoEventoService;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.evento.exceptions.TimeInvalidException;
import com.diacono.diacono.evento.mapper.EventoMapper;
import com.diacono.diacono.evento.mapper.EventoUpdateMapper;
import com.diacono.diacono.evento.model.dto.request.EventoCreateDTO;
import com.diacono.diacono.evento.model.dto.request.EventoUpdateDTO;
import com.diacono.diacono.evento.model.dto.response.EventoCompletoDTO;
import com.diacono.diacono.evento.model.dto.response.EventoSimplificadoDTO;
import com.diacono.diacono.evento.model.entity.DiasSemanaRecorrencia;
import com.diacono.diacono.evento.model.entity.Evento;
import com.diacono.diacono.evento.model.entity.TipoRecorrencia;
import com.diacono.diacono.evento.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.membro.service.MembroService;
import com.diacono.diacono.ministerio.service.MinisteriosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;
    private final IgrejaService igrejaService;
    private final MinisteriosService ministerioService;
    private final MembroService membroService;
    private final OcorrenciaService ocorrenciaService;
    private final EnderecoEventoMapper enderecoEventoMapper;
    private final EnderecoEventoService enderecoEventoService;
    private final EventoUpdateMapper eventoUpdateMapper;

    public EventoService(EventoRepository eventoRepository, EventoMapper eventoMapper, IgrejaService igrejaService, MinisteriosService ministerioService, MembroService membroService, OcorrenciaService ocorrenciaService, EnderecoEventoMapper enderecoEventoMapper, EnderecoEventoService enderecoEventoService, EventoUpdateMapper eventoUpdateMapper) {
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
        this.igrejaService = igrejaService;
        this.ministerioService = ministerioService;
        this.membroService = membroService;
        this.ocorrenciaService = ocorrenciaService;
        this.enderecoEventoMapper = enderecoEventoMapper;
        this.enderecoEventoService = enderecoEventoService;
        this.eventoUpdateMapper = eventoUpdateMapper;
    }

    public EventoSimplificadoDTO buscarEventosPorMesEAno(int mes, int ano){

        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDate inicioMes = anoMes.atDay(1);
        LocalDate fimMes = anoMes.atEndOfMonth();

        Year anoAtual = Year.of(ano);
        LocalDate fimAno = anoAtual.atDay(anoAtual.length());
        LocalDate inicioAno = Year.of(ano).atDay(1);

        //validações necessárias (mes dentro de 1 e 12) e eventos vazio
        List<Evento> todosOsEventosMes = new ArrayList<>();
        List<Evento> eventosAno = eventoRepository.findByPeriodo(inicioAno, fimAno);

        int totalAno = 0;
        int totalSemana = calcularTotalSemana();

        for(Evento evento: eventosAno){

            if(evento.getTipoRecorrencia() == TipoRecorrencia.NAO_REPETE){
                if (!evento.getData().isBefore(inicioMes) && !evento.getData().isAfter(fimMes)) {
                    todosOsEventosMes.add(evento);
                }
                totalAno++;
            }else{
                todosOsEventosMes.addAll(ocorrenciaService.gerarEvento(evento, inicioMes, fimMes));
                totalAno += ocorrenciaService.contarOcorrencias(evento, inicioAno, fimAno);
            }

        }

        int totalMes = todosOsEventosMes.size();

        EventoSimplificadoDTO eventoResponse = eventoMapper.paraEventoSimplificado(todosOsEventosMes, totalSemana, totalMes, totalAno);
        return eventoResponse;
    }

    public EventoCompletoDTO buscarEventoEspecifico(UUID id, LocalDate dataHoje){
        //Validar dataHoje preenchida
        //validar a existencia do evento, se n existir lançar exceção
        Evento evento = eventoRepository.findByIdExterno(id);

        Evento eventoOcorrencia;

        if (evento.getTipoRecorrencia() == TipoRecorrencia.NAO_REPETE) {
            eventoOcorrencia = evento;
        } else {
            eventoOcorrencia = ocorrenciaService.criarOcorrencia(evento, dataHoje);

        }

        EventoCompletoDTO eventoResponse = eventoMapper.paraEventoCompletoDTO(eventoOcorrencia);

        return eventoResponse;
    }

    @Transactional
    public RestResponseMessage criarEvento(EventoCreateDTO request){

        /*EM UM FUTURO MELHOR AS QUERYS DE BUSCA DE MINISTERIO E ORGANIZADOR, LEVANDO-SE
        * EM CONSIDERAÇÃO A IGREJA DONA*/

        // completo, fazer ajuste no futuro para encontrar com fk igreja

        validaHoraInicioMenorHoraFim(request.horaInicio(), request.horaFim());

        if (request.tipoRecorrencia() != TipoRecorrencia.NAO_REPETE) {
            validarDatasDeRecorrencia(request.dataInicioRecorrencia(), request.dataTerminoRecorrencia());
            validarHoraRecorrencia(request.horarioRecorrencia());
            validarIntervaloRecorrencia(request.intervaloRecorrencia());
        }

        if (request.tipoRecorrencia() == TipoRecorrencia.SEMANAL) {
            validarDiasSemanaRecorrencia(request.diasSemana());
        }

        Evento evento = eventoMapper.paraEvento(request);
        EnderecoEvento enderecoEvento = criarEnderecoEvento(request.endereco());

        evento.setEnderecoEvento(enderecoEvento);
        evento.setIgreja(igrejaService.buscarUUID(request.fkIgreja()));
        evento.setOrganizador(membroService.buscarPorUUID(request.fkOrganizador()));
        evento.setMinisterios(ministerioService.buscarPorUUID(request.fkMinisterios()));

        return criarEventoMestre(evento);

    }

    @Transactional
    public RestResponseMessage apagarEvento(UUID idExterno){

        //completo

        validarIdExternoPreenchido(idExterno);

        long deleteCount = eventoRepository.deleteByIdExterno(idExterno);

        if(deleteCount == 0){
            throw new ObjectSaveErrorException("Não foi possível apagar o evento");
        }

        RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Evento apagado com sucesso");

        return message;

    }

    @Transactional
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

        Evento eventoAtualizado = eventoRepository.save(evento);

        if(eventoAtualizado == null){
            throw new ObjectSaveErrorException("Não foi possível atualizar o evento");
        }

        return new RestResponseMessage(HttpStatus.OK, "Evento atualizado com sucesso");
    }







    /*MÉTODOS AUXILIARES -> CONTEM LÓGICAS PARA UTILIZAR EM OUTROS MÉTODOS*/

    private void validaHoraInicioMenorHoraFim(LocalTime inicio, LocalTime fim){
        if(fim.isBefore(inicio)){
            throw new TimeInvalidException("O horário de término do evento precisa ser maior que o horário de início");
        }
    }

    private void validarDatasDeRecorrencia(LocalDate inicio, LocalDate fim) {
        if (fim.isBefore(inicio)) {
            throw new DateTimeException("A data final da recorrência precisa ser maior ou igual à data de início.");
        }
    }

    private void validarDiasSemanaRecorrencia(List<DiasSemanaRecorrencia> dias){

        if (dias == null || dias.isEmpty()) {
            throw new FieldInvalidException("Para recorrências semanais, é obrigatório informar os dias da semana.");
        }
    }

    private void validarHoraRecorrencia(LocalTime horarioRecorrencia){

        if (Objects.isNull(horarioRecorrencia)) {
            throw new TimeInvalidException("Para eventos com recorrência é obrigatório selecionar o horário da recorrência.");
        }
    }

    private void validarIntervaloRecorrencia(int value){

        if (value < 0){
            throw new FieldInvalidException("Para eventos com recorrência, é obrigatório selecionar um intervalo maior que zero.");
        }
    }

    private void validarIdExternoPreenchido(UUID idExterno){
        if(idExterno == null){
            throw new FieldInvalidException("O id do evento precisa ser informado");
        }
    }

    private void validarEventoExistente(Evento evento){
        if(evento == null){
            throw new ObjectSaveErrorException("Evento não encontrado");
        }
    }

    private EnderecoEvento criarEnderecoEvento(EnderecoEventoDTO endereco){
        EnderecoEvento enderecoEvento;

        if (endereco.idExterno() != null) {
            enderecoEvento = enderecoEventoService.buscarPorUUID(endereco.idExterno());
        } else {
            enderecoEvento = enderecoEventoMapper.paraEndereco(endereco);
            enderecoEventoService.salvarEnderecoEvento(enderecoEvento);
        }

        if(enderecoEvento == null){
            throw new ObjectSaveErrorException("Não foi possível criar o endereço do evento");
        }

        return enderecoEvento;
    }










    private RestResponseMessage criarEventoMestre(Evento evento){
        /*VALIDAR SE FOI POSSÍVEL CRIAR OU NÃO -- GERA EXCEÇÃO*/
        /*MELHORAR O RETORNO DO MÉTODO*/

        Evento salvo = eventoRepository.save(evento);

        if(salvo == null){
            throw new ObjectSaveErrorException("Não foi possível criar o evento");
        }

        RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Evento criado com sucesso");

        return message;

    }

    private int calcularTotalSemana() {
        LocalDate hoje = LocalDate.now();

        LocalDate inicioSemana = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate fimSemana = hoje.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        ArrayList<Evento> eventosMestres = eventoRepository.findByPeriodo(inicioSemana, fimSemana);

        int totalSemana = 0;

        for (Evento evento : eventosMestres) {

            if (evento.getTipoRecorrencia() == TipoRecorrencia.NAO_REPETE) {
                if (!evento.getData().isBefore(inicioSemana) && !evento.getData().isAfter(fimSemana)) {
                    totalSemana++;
                }
            } else {
                totalSemana += ocorrenciaService.contarOcorrencias(evento, inicioSemana, fimSemana);
            }
        }
        return totalSemana;
    }



}
