package com.diacono.diacono.evento.mapper;

import com.diacono.diacono.evento.model.dto.request.EventoCreateDTO;
import com.diacono.diacono.evento.model.dto.response.EventoCompletoDTO;
import com.diacono.diacono.evento.model.dto.response.EventoSimplificadoDTO;
import com.diacono.diacono.evento.model.dto.response.EventoUnicoSimplificadoDTO;
import com.diacono.diacono.evento.model.entity.Evento;
import com.diacono.diacono.endereco.mapper.EnderecoMapper;
import com.diacono.diacono.Igreja.mapper.IgrejaMapper;
import com.diacono.diacono.membro.mapper.MembroMapper;
import com.diacono.diacono.ministerio.mapper.MinisterioMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;

@Mapper(componentModel = "spring",
        uses = {MembroMapper.class, MinisterioMapper.class, EnderecoMapper.class, IgrejaMapper.class})
public interface EventoMapper {

    EventoUnicoSimplificadoDTO paraEventoUnicoSimplificadoDTO(Evento evento);

    ArrayList<EventoUnicoSimplificadoDTO> paraListaEventoUnicoSimplificadoDTO(ArrayList<Evento> eventos);

    default EventoSimplificadoDTO paraEventoSimplificado(ArrayList<Evento> eventos, int totalSemana, int totalMes, int totalAno) {

        ArrayList<EventoUnicoSimplificadoDTO> listaMapeada = paraListaEventoUnicoSimplificadoDTO(eventos);

        return new EventoSimplificadoDTO(
                listaMapeada,
                totalSemana,
                totalMes,
                totalAno
        );
    }

    EventoCompletoDTO paraEventoCompletoDTO(Evento evento);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    @Mapping(target = "igreja", ignore = true)
    @Mapping(target = "organizador", ignore = true)
    @Mapping(target = "ministerios", ignore = true)
    Evento paraEvento(EventoCreateDTO request);
}
