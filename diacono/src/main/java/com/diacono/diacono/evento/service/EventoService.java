package com.diacono.diacono.evento.service;

import com.diacono.diacono.Igreja.service.IgrejaService;
import com.diacono.diacono.evento.mapper.EventoMapper;
import com.diacono.diacono.evento.model.dto.request.EventoCreateDTO;
import com.diacono.diacono.evento.model.dto.response.EventoCompletoDTO;
import com.diacono.diacono.evento.model.dto.response.EventoSimplificadoDTO;
import com.diacono.diacono.evento.model.entity.Evento;
import com.diacono.diacono.evento.model.entity.TipoRecorrencia;
import com.diacono.diacono.evento.repository.EventoRepository;
import com.diacono.diacono.membro.service.MembroService;
import com.diacono.diacono.ministerio.service.MinisteriosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;
    private final IgrejaService igrejaService;
    private final MinisteriosService ministerioService;
    private final MembroService membroService;
    private final OcorrenciaService ocorrenciaService;

    public EventoService(EventoRepository eventoRepository, EventoMapper eventoMapper, IgrejaService igrejaService, MinisteriosService ministerioService, MembroService membroService, OcorrenciaService ocorrenciaService) {
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
        this.igrejaService = igrejaService;
        this.ministerioService = ministerioService;
        this.membroService = membroService;
        this.ocorrenciaService = ocorrenciaService;
    }

    public EventoSimplificadoDTO buscarEventosPorMesEAno(int mes, int ano){

        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDate inicioMes = anoMes.atDay(1);
        LocalDate fimMes = anoMes.atEndOfMonth();

        Year anoAtual = Year.of(ano);
        LocalDate fimAno = anoAtual.atDay(anoAtual.length());
        LocalDate inicioAno = Year.of(ano).atDay(1);

        //validações necessárias (mes dentro de 1 e 12) e eventos vazio
        ArrayList<Evento> todosOsEventosMes = new ArrayList<>();
        ArrayList<Evento> eventosAno = eventoRepository.findByPeriodo(inicioAno, fimAno);

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

    public EventoCompletoDTO buscarEventoEspecifico(UUID id){

        //validar a existencia do evento, se n existir lançar exceção
        Evento evento = eventoRepository.findByIdExterno(id);

        EventoCompletoDTO eventoResponse = eventoMapper.paraEventoCompletoDTO(evento);

        return eventoResponse;
    }

    @Transactional
    public String criarEvento(EventoCreateDTO request){

        /*EM UM FUTURO MELHOR AS QUERYS DE BUSCA DE MINISTERIO E ORGANIZADOR, LEVANDO-SE
        * EM CONSIDERAÇÃO A IGREJA DONA*/

        Evento evento = eventoMapper.paraEvento(request);
        evento.setIgreja(igrejaService.buscarUUID(request.fkIgreja()));
        evento.setOrganizador(membroService.buscarPorUUID(request.fkOrganizador()));
        evento.setMinisterios(ministerioService.buscarPorUUID(request.fkMinisterios()));

        return criarEventoMestre(evento);

    }



    /*MÉTODOS AUXILIARES -> CONTEM LÓGICAS PARA UTILIZAR EM OUTROS MÉTODOS*/

    private String criarEventoMestre(Evento evento){
        /*VALIDAR SE FOI POSSÍVEL CRIAR OU NÃO -- GERA EXCEÇÃO*/
        /*MELHORAR O RETORNO DO MÉTODO*/

        Evento salvo = eventoRepository.save(evento);
        return "Evento criado com sucesso";

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
