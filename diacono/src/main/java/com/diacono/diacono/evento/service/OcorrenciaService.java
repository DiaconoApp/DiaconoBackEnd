package com.diacono.diacono.evento.service;

import com.diacono.diacono.evento.model.entity.DiasSemanaRecorrencia;
import com.diacono.diacono.evento.model.entity.Evento;
import com.diacono.diacono.evento.model.entity.TipoRecorrencia;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class OcorrenciaService {


    protected ArrayList<Evento> gerarEvento(Evento evento, LocalDate inicio, LocalDate fim){

        ArrayList<Evento> eventos = new ArrayList<>();

        LocalDate dataInicioRecorrencia = evento.getDataInicioRecorrencia();
        LocalDate dataLimiteOcorrencia = evento.getDataTerminoRecorrencia();
        LocalDate limiteFinal = dataLimiteOcorrencia.isBefore(fim) ? dataLimiteOcorrencia : fim;

        dataInicioRecorrencia = avancarParaPontoDePartida(dataInicioRecorrencia, evento, inicio, dataLimiteOcorrencia);

        while (!dataInicioRecorrencia.isAfter(limiteFinal)) {

            switch (evento.getTipoRecorrencia()) {

                case DIARIO:
                case MENSAL:
                case ANUAL:
                    if (!dataInicioRecorrencia.isBefore(inicio) && !dataInicioRecorrencia.isAfter(limiteFinal)) {
                        Evento ocorrencia = criarOcorrencia(evento, dataInicioRecorrencia);
                        eventos.add(ocorrencia);
                    }

                    dataInicioRecorrencia = switch (evento.getTipoRecorrencia()) {
                        case DIARIO -> dataInicioRecorrencia.plusDays(evento.getIntervaloRecorrencia());
                        case MENSAL -> dataInicioRecorrencia.plusMonths(evento.getIntervaloRecorrencia());
                        case ANUAL -> dataInicioRecorrencia.plusYears(evento.getIntervaloRecorrencia());
                        default -> dataInicioRecorrencia;
                    };
                    break;

                case SEMANAL:
                    for (DiasSemanaRecorrencia diaRecorrente : evento.getDiasSemana()) {

                        DayOfWeek diaAlvo = diaRecorrente.getDayOfWeek();

                        LocalDate proximaOcorrencia = dataInicioRecorrencia.with(TemporalAdjusters.nextOrSame(diaAlvo));

                        if (!proximaOcorrencia.isAfter(limiteFinal) && !proximaOcorrencia.isBefore(inicio)) {

                            Evento ocorrencia = criarOcorrencia(evento, proximaOcorrencia);
                            eventos.add(ocorrencia);
                        }
                    }

                    dataInicioRecorrencia = dataInicioRecorrencia.plusWeeks(evento.getIntervaloRecorrencia());
                    break;

                default: return eventos;
            }
        }

        return eventos;
    }

    public int contarOcorrencias(Evento evento, LocalDate inicioAno, LocalDate fimAno){

        int contador = 0;

        LocalDate dataAtual = evento.getDataInicioRecorrencia();
        LocalDate limiteRegra = evento.getDataTerminoRecorrencia();
        LocalDate limiteFinal = limiteRegra.isBefore(fimAno) ? limiteRegra : fimAno;

        if (evento.getTipoRecorrencia() == TipoRecorrencia.NAO_REPETE) {
            if (!dataAtual.isBefore(inicioAno) && !dataAtual.isAfter(limiteFinal)) {
                return 1;
            }
            return 0;
        }

        dataAtual = avancarParaPontoDePartida(dataAtual, evento, inicioAno, limiteRegra);

        if (dataAtual.isAfter(limiteRegra)) return 0;

        while (!dataAtual.isAfter(limiteRegra)) {

            switch (evento.getTipoRecorrencia()) {

                case DIARIO:
                case MENSAL:
                case ANUAL:
                    if (!dataAtual.isBefore(inicioAno) && !dataAtual.isAfter(limiteFinal)) {
                        contador++;
                    }

                    dataAtual = switch (evento.getTipoRecorrencia()) {
                        case DIARIO -> dataAtual.plusDays(evento.getIntervaloRecorrencia());
                        case MENSAL -> dataAtual.plusMonths(evento.getIntervaloRecorrencia());
                        case ANUAL -> dataAtual.plusYears(evento.getIntervaloRecorrencia());
                        default -> dataAtual;
                    };
                    break;

                case SEMANAL:
                    for (DiasSemanaRecorrencia diaRecorrente : evento.getDiasSemana()) {

                        DayOfWeek diaAlvo = diaRecorrente.getDayOfWeek();

                        LocalDate proximaOcorrencia = dataAtual.with(TemporalAdjusters.nextOrSame(diaAlvo));

                        if (!proximaOcorrencia.isAfter(limiteFinal) && !proximaOcorrencia.isBefore(inicioAno)) {
                            contador++;
                        }
                    }

                    dataAtual = dataAtual.plusWeeks(evento.getIntervaloRecorrencia());
                    break;

                default:
                    return contador;
            }
        }

        return contador;


    }


    /*METODOS AUXILIARES*/
    private LocalDate avancarParaPontoDePartida(LocalDate dataAtual, Evento evento, LocalDate inicio, LocalDate limiteRegra) {

        while (dataAtual.isBefore(inicio) && !dataAtual.isAfter(limiteRegra)) {
            dataAtual = switch (evento.getTipoRecorrencia()) {
                case DIARIO -> dataAtual.plusDays(evento.getIntervaloRecorrencia());
                case SEMANAL -> dataAtual.plusWeeks(evento.getIntervaloRecorrencia());
                case MENSAL -> dataAtual.plusMonths(evento.getIntervaloRecorrencia());
                case ANUAL -> dataAtual.plusYears(evento.getIntervaloRecorrencia());
                default -> limiteRegra.plusDays(1);
            };
        }
        return dataAtual;
    }

    private Evento criarOcorrencia(Evento eventoMestre, LocalDate novaData) {
        return eventoMestre.toBuilder()
                .data(novaData)
                .tipoRecorrencia(TipoRecorrencia.NAO_REPETE)
                .build();
    }

}
