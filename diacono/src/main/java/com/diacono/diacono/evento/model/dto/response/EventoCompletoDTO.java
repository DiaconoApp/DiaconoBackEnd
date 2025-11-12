package com.diacono.diacono.evento.model.dto.response;

import com.diacono.diacono.Igreja.model.dto.response.IgrejaSimplificadoDTO;
import com.diacono.diacono.membro.model.dto.response.MembroSimplificadoDTO;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioSimplificadoDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record EventoCompletoDTO(
        String nome,
        String descricao,
        String publicoAlvo,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        BigDecimal custo,
        MembroSimplificadoDTO organizador,
        List<MinisterioSimplificadoDTO> ministerios,
        EnderecoEventoSimplificadoDTO enderecoEvento,
        IgrejaSimplificadoDTO igreja,
        RecorrenciaSimplificadaDTO recorrencia

) {
}
