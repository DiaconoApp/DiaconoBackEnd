package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.evento.EventoSimplificadoDTO;
import com.diacono.diacono.applications.mappers.evento.EventoMapper;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class BuscarEventosPorMesEAnoUseCase {

    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;

    public BuscarEventosPorMesEAnoUseCase(
            EventoRepository eventoRepository,
            EventoMapper eventoMapper
    ) {
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
    }

    public EventoSimplificadoDTO execute(int mes, int ano, UUID igrejaId) {
        validarMesEAno(mes, ano);

        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDateTime inicioMes = anoMes.atDay(1).atStartOfDay();
        LocalDateTime fimMes = anoMes.atEndOfMonth().atTime(23, 59, 59);

        List<Evento> eventos = eventoRepository.findByPeriodo(inicioMes, fimMes, igrejaId);

        if (eventos == null || eventos.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum evento encontrado para o mês e ano informados");
        }

        return eventoMapper.paraEventoSimplificado(eventos);
    }

    private void validarMesEAno(int mes, int ano) {
        if (mes < 1 || mes > 12) {
            throw new FieldInvalidException("O mês precisa estar entre 1 e 12");
        }

        if (ano <= 0) {
            throw new FieldInvalidException("O ano precisa ser maior que 0");
        }
    }
}