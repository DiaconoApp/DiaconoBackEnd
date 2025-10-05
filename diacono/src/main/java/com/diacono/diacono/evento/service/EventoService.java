package com.diacono.diacono.evento.service;

import com.diacono.diacono.evento.mapper.EventoMapper;
import com.diacono.diacono.evento.model.dto.response.EventoCompletoDTO;
import com.diacono.diacono.evento.model.dto.response.EventoSimplificadoDTO;
import com.diacono.diacono.evento.model.entity.Evento;
import com.diacono.diacono.evento.repository.EventoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;

    public EventoService(EventoRepository eventoRepository, EventoMapper eventoMapper) {
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
    }

    public EventoSimplificadoDTO buscarEventosPorMesEAno(int mes, int ano){
        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDate inicio = anoMes.atDay(1);
        LocalDate fim = anoMes.atEndOfMonth();

        //validações necessárias (mes dentro de 1 e 12) e eventos vazio
        List<Evento> eventos = eventoRepository.findByPeriodo(inicio, fim);
        int totalSemana = totalSemana();
        int totalMes = totalMes(inicio, fim);
        int totalAno = totalAno(ano);
        EventoSimplificadoDTO eventoResponse = eventoMapper.paraEventoSimplificado(eventos, totalSemana, totalMes, totalAno);

        return eventoResponse;
    }

    public EventoCompletoDTO buscarEventoEspecifico(UUID id){

        //validar a existencia do evento, se n existir lançar exceção
        Evento evento = eventoRepository.findByIdExterno(id);

        EventoCompletoDTO eventoResponse = eventoMapper.paraEventoCompletoDTO(evento);

        return eventoResponse;
    }


    public int totalSemana(){
        LocalDate hoje = LocalDate.now();
        LocalDate diaInicioSemana = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate diaFimSemana = hoje.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return eventoRepository.countEventosNoPeriodo(diaInicioSemana, diaFimSemana);
    }

    public int totalMes(LocalDate inicio, LocalDate fim){
        return eventoRepository.countEventosNoPeriodo(inicio, fim);
    }

    public int totalAno(int ano){
        Year anoAtual = Year.of(ano);
        LocalDate inicio = anoAtual.atDay(1);
        LocalDate fim = anoAtual.atDay(anoAtual.length());
        return eventoRepository.countEventosNoPeriodo(inicio, fim);
    }

}
