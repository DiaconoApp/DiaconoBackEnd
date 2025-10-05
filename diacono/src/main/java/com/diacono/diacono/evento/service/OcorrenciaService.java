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

        while (dataInicioRecorrencia.isBefore(inicio) && !dataInicioRecorrencia.isAfter(limiteFinal)) {

            switch (evento.getTipoRecorrencia()) {
                case DIARIO: dataInicioRecorrencia = dataInicioRecorrencia.plusDays(evento.getIntervaloRecorrencia()); break;
                case SEMANAL: dataInicioRecorrencia = dataInicioRecorrencia.plusWeeks(evento.getIntervaloRecorrencia()); break;
                case MENSAL: dataInicioRecorrencia = dataInicioRecorrencia.plusMonths(evento.getIntervaloRecorrencia()); break;
                case ANUAL: dataInicioRecorrencia = dataInicioRecorrencia.plusYears(evento.getIntervaloRecorrencia()); break;
                default: return eventos;
            }

        }

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


    private Evento criarOcorrencia(Evento eventoMestre, LocalDate novaData) {
        return eventoMestre.toBuilder()
                .data(novaData)
                .tipoRecorrencia(TipoRecorrencia.NAO_REPETE)
                .build();
    }

}
