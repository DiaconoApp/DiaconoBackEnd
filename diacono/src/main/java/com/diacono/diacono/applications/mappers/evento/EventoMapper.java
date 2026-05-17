package com.diacono.diacono.applications.mappers.evento;

import com.diacono.diacono.applications.dtos.evento.EventoCreateDTO;
import com.diacono.diacono.applications.dtos.evento.EventoUpdateDTO;
import com.diacono.diacono.applications.dtos.evento.EventoCompletoDTO;
import com.diacono.diacono.applications.dtos.evento.EventoSimplificadoDTO;
import com.diacono.diacono.applications.dtos.evento.EventoUnicoSimplificadoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.applications.mappers.igreja.IgrejaMapper;
import com.diacono.diacono.applications.mappers.ministerio.MinisterioMapper;
import com.diacono.diacono.applications.mappers.endereco.EnderecoEventoMapper;
import com.diacono.diacono.applications.mappers.membro.MembroMapper;
import com.diacono.diacono.domain.entity.EscalaEvento;
import com.diacono.diacono.domain.entity.Evento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        uses = {MembroMapper.class, MinisterioMapper.class, EnderecoEventoMapper.class, IgrejaMapper.class})
public interface EventoMapper {

    EventoUnicoSimplificadoDTO paraEventoUnicoSimplificadoDTO(Evento evento);

    List<EventoUnicoSimplificadoDTO> paraListaEventoUnicoSimplificadoDTO(List<Evento> eventos);

    default EventoSimplificadoDTO paraEventoSimplificado(List<Evento> eventos) {

        List<EventoUnicoSimplificadoDTO> listaMapeada = paraListaEventoUnicoSimplificadoDTO(eventos);

        return new EventoSimplificadoDTO(
                listaMapeada
        );
    }

    @Mapping(target = "ministerios", source = "escalaEvento")
    EventoCompletoDTO paraEventoCompletoDTO(Evento evento);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    @Mapping(target = "igreja", ignore = true)
    @Mapping(target = "organizador", ignore = true)
    @Mapping(target = "escalaEvento", ignore = true)
    @Mapping(target = "recorrencia", ignore = true)
    Evento paraEvento(EventoCreateDTO request);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    @Mapping(target = "igreja", ignore = true)
    @Mapping(target = "organizador", ignore = true)
    @Mapping(target = "escalaEvento", ignore = true)
    Evento paraEvento(EventoUpdateDTO request);

    default List<MinisterioSimplificadoDTO> mapEscalaEventoParaMinisterios(Set<EscalaEvento> escalasEvento) {
        if (escalasEvento == null || escalasEvento.isEmpty()) {
            return List.of();
        }

        return escalasEvento.stream()
                .filter(escalaEvento -> escalaEvento.getMinisterio() != null)
                .map(escalaEvento -> new MinisterioSimplificadoDTO(
                        escalaEvento.getMinisterio().getIdExterno(),
                        escalaEvento.getMinisterio().getNome(),
                        escalaEvento.getMinisterio().getNomeLider(),
                        escalaEvento.getMinisterio().getStatus(),
                        escalaEvento.getMinisterio().getDataCriacao()
                ))
                .toList();
    }
}
