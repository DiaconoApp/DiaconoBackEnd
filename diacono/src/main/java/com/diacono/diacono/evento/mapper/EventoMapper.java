package com.diacono.diacono.evento.mapper;

import com.diacono.diacono.evento.model.dto.response.EventoCompletoDTO;
import com.diacono.diacono.evento.model.dto.response.EventoSimplificadoDTO;
import com.diacono.diacono.evento.model.dto.response.EventoUnicoSimplificadoDTO;
import com.diacono.diacono.evento.model.entity.Evento;
import com.diacono.diacono.global.mapper.EnderecoMapper;
import com.diacono.diacono.global.mapper.IgrejaMapper;
import com.diacono.diacono.membro.mapper.MembroMapper;
import com.diacono.diacono.ministerio.mapper.MinisterioMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {MembroMapper.class, MinisterioMapper.class, EnderecoMapper.class, IgrejaMapper.class})
public interface EventoMapper {

    EventoUnicoSimplificadoDTO paraEventoUnicoSimplificadoDTO(Evento evento);

    List<EventoUnicoSimplificadoDTO> paraListaEventoUnicoSimplificadoDTO(List<Evento> eventos);

    default EventoSimplificadoDTO paraEventoSimplificado(List<Evento> eventos, int totalSemana, int totalMes, int totalAno) {

        List<EventoUnicoSimplificadoDTO> listaMapeada = paraListaEventoUnicoSimplificadoDTO(eventos);

        return new EventoSimplificadoDTO(
                listaMapeada,
                totalSemana,
                totalMes,
                totalAno
        );
    }

    EventoCompletoDTO paraEventoCompletoDTO(Evento evento);
}
