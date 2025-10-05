package com.diacono.diacono.evento.model.dto.response;

import com.diacono.diacono.evento.model.entity.Evento;
import com.diacono.diacono.global.model.dto.response.EnderecoSimplificadoDTO;
import com.diacono.diacono.global.model.dto.response.IgrejaSimplificadoDTO;
import com.diacono.diacono.membro.model.dto.response.MembroSimplificadoDTO;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioSimplificadoDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record EventoCompletoDTO(
        String nome,
        String descricao,
        String publicoAlvo,
        LocalDate data,
        LocalTime horaInicio,
        LocalTime horaFim,
        BigDecimal custo,
        MembroSimplificadoDTO organizador,
        List<MinisterioSimplificadoDTO> ministerios,
        EnderecoSimplificadoDTO endereco,
        IgrejaSimplificadoDTO igreja

) {
}
