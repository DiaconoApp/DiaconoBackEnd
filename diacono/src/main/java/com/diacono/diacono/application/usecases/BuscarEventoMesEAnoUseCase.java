package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.mappers.EventoMapper;
import com.diacono.diacono.domain.entities.Evento;
import com.diacono.diacono.application.exceptions.FieldInvalidException;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.EventoRepository;
import com.diacono.diacono.presentation.dto.response.EventoSimplificadoDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class BuscarEventoMesEAnoUseCase {

    private final EventoRepository repository;
    private final EventoMapper mapper;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public BuscarEventoMesEAnoUseCase(EventoRepository repository, EventoMapper mapper, JwtClaimsExtractor jwtClaimsExtractor) {
        this.repository = repository;
        this.mapper = mapper;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
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

        List<Evento> eventosAno = repository.findByPeriodo(inicioMes, fimMes, jwtClaimsExtractor.getIgrejaId());
        if(eventosAno.isEmpty() || eventosAno == null){
            throw new ObjectNotFoundException("Nenhum evento encontrado para o mês e ano informados");
        }

        EventoSimplificadoDTO eventoResponse = mapper.paraEventoSimplificado(eventosAno);
        return eventoResponse;
    }

}
