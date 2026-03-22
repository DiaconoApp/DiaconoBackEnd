package com.diacono.diacono.presentation.dto.response;

import com.diacono.diacono.presentation.dto.MinisterioSimplificadoDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
